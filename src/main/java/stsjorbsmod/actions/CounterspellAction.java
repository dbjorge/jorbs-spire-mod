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

    private static boolean isDebuffIntent(AbstractMonster.Intent intent) {
        return
            intent == AbstractMonster.Intent.STRONG_DEBUFF ||
            intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
            intent == AbstractMonster.Intent.DEBUFF ||
            intent == AbstractMonster.Intent.DEFEND_DEBUFF;
    }

    public void update() {
        if (this.monster != null && isDebuffIntent(this.monster.intent)) {
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(this.owner, this.owner, new ArtifactPower(this.owner, this.amount), this.amount));
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }

        this.isDone = true;
    }
}
