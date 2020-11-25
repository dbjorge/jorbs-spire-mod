package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.relics.LizardTail;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import stsjorbsmod.cards.DeathPreventionCard;
import stsjorbsmod.cards.cull.OldBook;
import stsjorbsmod.cards.cull.ShriekingHat;
import stsjorbsmod.powers.OldBookPower;
import stsjorbsmod.util.CardMetaUtils;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
public class DeathPreventionPatch {
    public static boolean triggerDeathPrevention(AbstractPlayer p, int damageAmount) {
        if (damageAmount < p.currentHealth)
            return false;
        if (!p.hasPower(OldBookPower.POWER_ID + OldBookPower.UPGRADED) &&
                !p.hasPower(OldBookPower.POWER_ID + OldBookPower.NORMAL) &&
                p.hand.group.stream().noneMatch(c -> c instanceof ShriekingHat))
            return false;
        if (p.hasPotion(FairyPotion.POTION_ID))
            return false;
        if (p.hasRelic(LizardTail.ID) && p.getRelic(LizardTail.ID).counter == -1)
            return false;

        // requested that death preventing cards trigger in order of pickup.
        SortedSet<DeathPreventionCard> deathPreventionCards = new TreeSet<>(Comparator.comparingInt(DeathPreventionCard::getPriority));
        for (AbstractCard c : p.hand.group) {
            if (c instanceof ShriekingHat) {
                deathPreventionCards.add((DeathPreventionCard) c);
            }
        }
        for (AbstractCard c : p.exhaustPile.group) {
            if (c instanceof OldBook) {
                deathPreventionCards.add((DeathPreventionCard) c);
            }
        }

        if (deathPreventionCards.isEmpty()) {
            return false;
        }
        AbstractCard firstDeathPreventionCard = (AbstractCard) deathPreventionCards.first();

        if (OldBook.ID.equals(firstDeathPreventionCard.cardID)) {
            handleOldBook(p);
        } else if (ShriekingHat.ID.equals(firstDeathPreventionCard.cardID)) {
            handleShriekingHat(p, (ShriekingHat) firstDeathPreventionCard);
        }

        return true;
    }

    private static void handleOldBook(AbstractPlayer p) {
        OldBookPower po;
        if (p.hasPower(OldBookPower.POWER_ID + OldBookPower.NORMAL))
            po = (OldBookPower) p.getPower(OldBookPower.POWER_ID + OldBookPower.NORMAL);
        else
            po = (OldBookPower) p.getPower(OldBookPower.POWER_ID + OldBookPower.UPGRADED);

        AbstractCard c = po.getCard();
        if (c.upgraded) {
            if (!p.hasRelic(MarkOfTheBloom.ID)) {
                float percent = (float) c.magicNumber / 100.0F;
                int healAmt = (int) ((float) p.maxHealth * percent);
                if (healAmt < 1) {
                    healAmt = 1;
                }

                p.heal(healAmt, true);
            } else {
                p.getRelic(MarkOfTheBloom.ID).flash();
            }
        }

        po.reducePower(1);
        if (po.amount <= 0)
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, po));

        CardMetaUtils.destroyCardPermanently(c);
    }

    private static void handleShriekingHat(AbstractPlayer p, ShriekingHat shriekingHatCard) {
        shriekingHatCard.use(p, null);
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class AbstractPlayer_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getClassName().equals(AbstractPlayer.class.getName()) &&
                            fieldAccess.getFieldName().equals("lastDamageTaken")) {
                        fieldAccess.replace(String.format("{ if (%1$s.triggerDeathPrevention($0, damageAmount)) { damageAmount = 0; lastDamageTaken = 0; }" +
                                        "else { $proceed($$); }; } ",
                                DeathPreventionPatch.class.getName()));
                    }
                }
            };
        }
    }
}
