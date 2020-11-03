package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.util.CardMetaUtils;
import stsjorbsmod.util.IntentUtils;
import stsjorbsmod.util.ReflectionUtils;

public class MirroredTechniquePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(MirroredTechniquePower.class);
    public static final String POWER_ID = STATIC.ID;

    public int extraPlays;

    public MirroredTechniquePower(final AbstractCreature owner, final int extraPlays) {
        super(STATIC);

        this.owner = owner;
        this.amount = -1;
        this.isTurnBased = false;
        this.extraPlays = extraPlays;

        updateDescription();
    }

    @Override
    public void stackPower(AbstractPower otherPower) {
        MirroredTechniquePower other = (MirroredTechniquePower) otherPower;
        this.extraPlays = Math.max(this.extraPlays, other.extraPlays);
    }

    @Override
    public AbstractPower makeCopy() {
        return new MirroredTechniquePower(owner, extraPlays);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        int multiUse = getIncomingAttackCount();
        if (card.type == AbstractCard.CardType.ATTACK && multiUse > 0 && card.purgeOnUse == false) {
            this.flash();
            AbstractMonster m = (AbstractMonster)action.target;

            for (int i = 0; i < multiUse + extraPlays; ++i) {
                CardMetaUtils.playCardAdditionalTime(card, m);
            }
        }
    }

    public static int getIncomingAttackCount() {
        int totalCount = 0;

        if(AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && IntentUtils.isAttackIntent(m.intent)) {

                    int multiAmt = 0;
                    //uses .equals(true) to force getPrivateField to boolean, getting around a compilation error with openJDK
                    if (ReflectionUtils.getPrivateField(m, AbstractMonster.class, "isMultiDmg").equals(true)) {
                        multiAmt = ReflectionUtils.getPrivateField(m, AbstractMonster.class, "intentMultiAmt");
                    }
                    else {
                        multiAmt = 1;
                    }
                    totalCount += multiAmt;
                }
            }
        }

        return totalCount;
    }

    @Override
    public void updateDescription() {
        if (this.extraPlays == 0) {
            this.description = DESCRIPTIONS[0];
        }
        else {
            this.description = String.format(DESCRIPTIONS[1], extraPlays);
        }
    }
}