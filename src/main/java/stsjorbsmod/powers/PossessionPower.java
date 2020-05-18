package stsjorbsmod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.CombatUtils;

public class PossessionPower extends CustomJorbsModPower{
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(PossessionPower.class);
    public static final String POWER_ID = STATIC.ID;
    private final AbstractPlayer p;

    public PossessionPower(final AbstractMonster owner, int amount, AbstractPlayer p) {
        super(STATIC);
        this.type = PowerType.DEBUFF;
        this.owner = owner;
        this.amount = amount;
        this.p = p;
        this.isTurnBased = true;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        for (int i = 0; i < this.amount; i++) {
            int count = 0;
            if (AbstractDungeon.getMonsters() != null) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDead && !monster.isDying) {
                        count += 1;
                    }
                }
            }
            if (count != 1) {
                    AbstractMonster m = CombatUtils.getRandomAliveMonster(AbstractDungeon.getMonsters(), candidate -> candidate != this.owner, AbstractDungeon.cardRandomRng);
                    addToBot(new DamageAction(m, new DamageInfo(this.owner, this.owner.maxHealth), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void onInitialApplication() {
        addToBot(new ApplyPowerAction(owner, this.p, new StunMonsterPower((AbstractMonster)owner, 1)));
    }

    @Override
    public void updateDescription() {
        description = amount == 1 ? DESCRIPTIONS[0] : String.format(DESCRIPTIONS[1], amount);
    }


    @Override
    public AbstractPower makeCopy() {
        return new PossessionPower((AbstractMonster) owner, amount, p);
    }

}
