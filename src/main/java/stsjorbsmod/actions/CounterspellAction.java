package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.IntentUtils;

public class CounterspellAction extends AbstractGameAction {
    private static final String UI_ID = JorbsMod.makeID(CounterspellAction.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractCreature owner;
    private AbstractMonster monster;

    public CounterspellAction(AbstractCreature owner, AbstractMonster monster, int artifactAmount) {
        this.owner = owner;
        this.actionType = ActionType.WAIT;
        this.amount = artifactAmount;
        this.monster = monster;
    }

    public void update() {
        if (this.monster != null && IntentUtils.isDebuffIntent(this.monster.intent)) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(this.owner, this.owner, new ArtifactPower(this.owner, this.amount), this.amount));
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }

        this.isDone = true;
    }
}
