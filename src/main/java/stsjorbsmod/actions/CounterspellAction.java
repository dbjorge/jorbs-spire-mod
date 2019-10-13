package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class CounterspellAction extends AbstractGameAction  {
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
        }

        this.isDone = true;
    }
}
