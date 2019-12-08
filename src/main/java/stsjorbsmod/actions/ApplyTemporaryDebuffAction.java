package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;

/**
 * This is used for cases like Kindness and Hold Monster where there is a debuff effect paired with setting up
 * an "undo" action (eg, adding an equivalent end of turn buff or storing an equivalent buff for later use)
 *
 * Putting the Artifact check + ApplyToPower in a wrapper action is important for the sake of Hold Monster, where
 * player the card can invoke two separate temporary debuff effects that need to consider Artifact sequentially
 */
public class ApplyTemporaryDebuffAction extends AbstractGameAction {
    private final ApplyPowerAction applyPowerAction;
    private final Runnable setupUndoDebuff;

    public ApplyTemporaryDebuffAction(ApplyPowerAction applyPowerAction, Runnable setupUndoDebuff) {
        this.applyPowerAction = applyPowerAction;
        this.setupUndoDebuff = setupUndoDebuff;
    }

    public void update() {
        AbstractDungeon.actionManager.addToTop(applyPowerAction);

        AbstractCreature target = applyPowerAction.target;
        if (target != null && !target.isDeadOrEscaped() && !target.hasPower(ArtifactPower.POWER_ID)) {
            setupUndoDebuff.run();
        }

        isDone = true;
    }
}
