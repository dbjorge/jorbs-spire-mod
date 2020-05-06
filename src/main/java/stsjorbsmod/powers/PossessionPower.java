package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;

public class PossessionPower extends CustomJorbsModPower{
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PossessionPower.class);
    public static final String POWER_ID = STATIC.ID;
    private final AbstractMonster target;

    public PossessionPower(AbstractMonster target, int amount) {
        super(STATIC);

        this.type = PowerType.DEBUFF;
        this.target = target;
        this.amount = amount;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractMonster m = this.target;
        JorbsMod.logger.info("m and this.target: " + m);
        int count = 0;
        if (AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                count += 1;
            }
        }
        JorbsMod.logger.info(count);
        if (count != 1) {
            JorbsMod.logger.info("more than 1");
            while (m == this.target) {
                m = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
            }
            JorbsMod.logger.info("monster chosen: " + m);
            addToBot(new DamageAction(m, new DamageInfo(this.target, this.target.maxHealth), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            JorbsMod.logger.info("Did damage");
            if (this.amount == 2) {
                JorbsMod.logger.info("Did damage2");
                addToBot(new DamageAction(m, new DamageInfo(this.target, this.target.maxHealth), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
        else {

        }
    }

    @Override
    public void updateDescription() {
        description = String.format(this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1]);
    }

    @Override
    public AbstractPower makeCopy() {
        return null;
                //new PossessionPower(target, amount);
    }

}
