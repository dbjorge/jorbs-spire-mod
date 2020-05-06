package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.lang.reflect.Field;

public class MirroredTechniquePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(MirroredTechniquePower.class);
    public static final String POWER_ID = STATIC.ID;

    private static Field multiIntentField;
    private static int extraPlays;

    public MirroredTechniquePower(final AbstractCreature owner, final int extraPlays) {
        super(STATIC);

        this.owner = owner;
        this.isTurnBased = false;
        this.extraPlays = extraPlays;

        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new MirroredTechniquePower(owner, amount);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        // Perhaps instead of onUseCard(...) I could use atDamageGive or onAttack

        int multiUse = getIncomingAttackCount();
        if (card.type == AbstractCard.CardType.ATTACK && multiUse > 0 && card.purgeOnUse == false) {
            this.flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster) action.target;
            }

            for (int i = 0; i < multiUse + extraPlays; i++) {
                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float) Settings.HEIGHT / 2.0F;
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }

                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
            }

        }
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

    public void updateDescription() {
        if (this.extraPlays == 0) {
            this.description = DESCRIPTIONS[0];
        }
        else {
            this.description = String.format(DESCRIPTIONS[1], extraPlays);
        }
    }
}