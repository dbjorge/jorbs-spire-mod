package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

// Courtesy of https://github.com/Alchyr/Chen/blob/master/src/main/java/Chen/Actions/GenericActions/EndTurnNowAction.java
public class EndTurnNowAction extends AbstractGameAction {
    @Override
    public void update() {
        AbstractDungeon.actionManager.cardQueue.clear();

        for (AbstractCard c : AbstractDungeon.player.limbo.group)
            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));

        AbstractDungeon.player.limbo.group.clear();
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.overlayMenu.endTurnButton.disable(true);

        this.isDone = true;
    }
}