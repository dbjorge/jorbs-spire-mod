package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// This covers AdvanceBuffsAndDebuffsThroughTimeAction and AdvanceRelicsThroughTimeAction
//
// Advancing through time is surprisingly complex (see #334, #410). In particular, it's
// important that callbacks for later points in time (eg, onEndOfTurn) do not get invoked
// until after callbacks for earlier points in time (eg, onStartOfTurn) have not only been
// invoked, but all the resulting actions have been *completed*. For example, if Arcane
// Weapon queues a DamageAction onEndOfTurn which will kill an Exploder, the ExplosivePower's
// next duringTurn callback must not be called before that DamageAction completes.
//
// Other complications to keep in mind:
//  * Arbitrary actions can queue arbitrary other actions
//  * Other actions may have been queued up behind AbstractTimeProgressionAction already
//    before it begins its work
//  * AbstractTimeProgressionAction is itself an Action, but must somehow span all of these
//  * There's probably some edge case where a Material Component can be played as an
//    end-of-turn effect, so it's possible for new AbstractTimeProgressionActions to become
//    queued as a result of playing out an AbstractTimeProgressionAction
//  * Actions queued as a result of the AbstractTimeProgressionAction might end the combat;
//    if this happens, it should stop further execution of the AbstractTimeProgressionAction
//
// The strategy we use is:
//  1 AbstractTimeProgressionAction maintains its own state machine of where in the emulated
//    turn progression it's currently at
//  2 The very first time it begins updating, it
//    - saves the current state of the action queue behind it
//    - clears the action queue
//  3 Then, it continues...
//    - invokes the next callback in turnProgression
//    - requeues itself at the bottom of the action queue
//  4 All the actions queued by the turnProgression callback execute
//  5 The (requeued) AbstractTimeProgressionAction begins again. This time, it:
//    - checks for any actions in the queue behind it.
//    - any AbstractTimeProgressionActions are taken out and added to the saved action queue
//    - if there are any other types of actions in the queue, requeue itself again and GOTO 4
//    - otherwise, if there are still more callbacks in turnProgression to perform, GOTO 3
//    - otherwise, requeue the saved action queue and finish
public class AbstractTimeProgressionAction extends AbstractGameAction {
    private boolean isFirstTime;
    private ArrayList<Runnable> allTurnsProgression = new ArrayList<>();
    private ArrayList<AbstractGameAction> savedActionQueue = new ArrayList<>();

    // This ctor must make a (shallow) copy of the turnProgression list, it may not take ownership of
    // the argument to modify later.
    protected AbstractTimeProgressionAction(List<Runnable> turnProgression, int numTurns) {
        isFirstTime = true;
        for (int i = 0; i < numTurns; ++i) {
            allTurnsProgression.addAll(turnProgression);
        }
    }

    // This ctor is only used when the old action is about to be discarded, so it takes
    // ownership of the existing lists
    private AbstractTimeProgressionAction(AbstractTimeProgressionAction oldActionToContinue) {
        this.allTurnsProgression = oldActionToContinue.allTurnsProgression;
        this.savedActionQueue = oldActionToContinue.savedActionQueue;
        isFirstTime = false;
    }

    @Override
    public final void update() {
        if (isFirstTime) {
            savedActionQueue.addAll(AbstractDungeon.actionManager.actions);
            AbstractDungeon.actionManager.actions.clear();
            isFirstTime = false;
        }

        for (Iterator<AbstractGameAction> i = AbstractDungeon.actionManager.actions.iterator(); i.hasNext();) {
            AbstractGameAction actionBelowUs = i.next();
            if (actionBelowUs instanceof AbstractTimeProgressionAction) {
                savedActionQueue.add(actionBelowUs);
                i.remove();
            }
        }

        if (!AbstractDungeon.actionManager.actions.isEmpty()) {
            requeueSelf();
            return;
        }

        if (!allTurnsProgression.isEmpty()) {
            Runnable nextPointInTime = allTurnsProgression.remove(0);
            nextPointInTime.run();
            requeueSelf();
            return;
        }

        AbstractDungeon.actionManager.actions.addAll(savedActionQueue);
        isDone = true;
    }

    private void requeueSelf() {
        // we can't *actually* requeue the same action because we need to set isDone = true to end
        // this iteration of the action, but end up with another version of ourselves with isDone = false
        // in the queue, so we queue a new action that is a copy of this one
        AbstractDungeon.actionManager.addToBottom(
                new AbstractTimeProgressionAction(this));
        this.isDone = true;
    }
}
