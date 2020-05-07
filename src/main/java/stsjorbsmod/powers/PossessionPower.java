package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;

public class PossessionPower extends CustomJorbsModPower{
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PossessionPower.class);
    public static final String POWER_ID = STATIC.ID;
    private final AbstractMonster target;

    public PossessionPower(AbstractMonster owner, int amount) {
        super(STATIC);
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        this.amount = amount;
        this.target = owner;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        AbstractMonster m = this.target;
        JorbsMod.logger.info("m and this.owner: " + m);

        while (m == this.target) {
            m = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
        }
        JorbsMod.logger.info("monster chosen: " + m);
        addToBot(new DamageAction(m, new DamageInfo(this.owner, this.owner.maxHealth), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        JorbsMod.logger.info("Did damage");
        if (this.amount == 2) {
            m = this.target;
            JorbsMod.logger.info("m and this.owner: " + m);

            while (m == this.target) {
                m = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
            }
            JorbsMod.logger.info("Did damage2");
            addToBot(new DamageAction(m, new DamageInfo(this.owner, this.owner.maxHealth), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void updateDescription() {
        description = this.amount == 1 ? DESCRIPTIONS[0] : String.format(DESCRIPTIONS[1], this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return null;
                //new PossessionPower(owner, amount);
    }

}
