package stsjorbsmod.powers;

import basemod.interfaces.OnCardUseSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.sun.corba.se.spi.copyobject.ReflectiveCopyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.ReflectionUtils;

import java.lang.reflect.Field;

public class MirroredTechniquePower extends CustomJorbsModPower implements OnCardUseSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(MirroredTechniquePower.class);
    public static final String POWER_ID = STATIC.ID;

    private static Field multiIntentField;

    public MirroredTechniquePower(final AbstractCreature owner, final int extraPlays) {
        super(STATIC);

        this.owner = owner;
        this.isTurnBased = false;

        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new MirroredTechniquePower(owner, amount);
    }

    public static int getIncomingAttackCount() {
        int totalCount = 0;

        // Some way to turn this into a call to ReflectionUtils.getPrivateField(...)
        if(multiIntentField == null) {
            try {
                multiIntentField = AbstractMonster.class.getDeclaredField("intentMultiAmt");
                multiIntentField.setAccessible(true);
            } catch (NoSuchFieldException e) { }
        }

        if(AbstractDungeon.getMonsters() != null) {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDeadOrEscaped() && isAttacking(m)) {
                    int multiAmt = 0;
                    try {
                        multiAmt = (int) multiIntentField.get(m);
                    }
                    catch (IllegalAccessException ignored) {
                    }
                    totalCount += multiAmt;
                }
            }
        }

        return totalCount;
    }

    public static boolean isAttacking(AbstractMonster m) {
        return m.intent == AbstractMonster.Intent.ATTACK || m.intent == AbstractMonster.Intent.ATTACK_BUFF || m.intent == AbstractMonster.Intent.ATTACK_DEBUFF || m.intent == AbstractMonster.Intent.ATTACK_DEFEND;
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {

    }
}