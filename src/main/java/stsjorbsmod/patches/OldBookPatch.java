package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
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
import stsjorbsmod.cards.cull.OldBook;
import stsjorbsmod.powers.OldBookPower;
import stsjorbsmod.util.CardMetaUtils;

@SpirePatch(clz = DamageInfo.class, method = SpirePatch.CLASS)
public class OldBookPatch {
    public static boolean triggerOldBook(AbstractPlayer p, int damageAmount) {
        if (damageAmount < p.currentHealth)
            return false;
        if (!p.hasPower(OldBookPower.POWER_ID + OldBookPower.UPGRADED) &&
            !p.hasPower(OldBookPower.POWER_ID + OldBookPower.NORMAL))
            return false;
        if (p.hasPotion(FairyPotion.POTION_ID))
            return false;
        if (p.hasRelic(LizardTail.ID) && ((LizardTail) p.getRelic(LizardTail.ID)).counter == -1)
            return false;

        OldBookPower po = null;
        if (p.hasPower(OldBookPower.POWER_ID + OldBookPower.NORMAL))
            po = (OldBookPower)p.getPower(OldBookPower.POWER_ID + OldBookPower.NORMAL);
        else
            po = (OldBookPower)p.getPower(OldBookPower.POWER_ID + OldBookPower.UPGRADED);

        AbstractCard c = po.getCard();
        if (c.upgraded) {
            if (!p.hasRelic(MarkOfTheBloom.ID)) {
                float percent = (float) c.magicNumber / 100.0F;
                int healAmt = (int) ((float) p.maxHealth * percent);
                if (healAmt < 1) {
                    healAmt = 1;
                }

                p.heal(healAmt, true);
            }
            else {
                ((MarkOfTheBloom) p.getRelic(MarkOfTheBloom.ID)).flash();
            }
        }

        po.reducePower(1);
        if (po.amount <= 0)
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, po));

        CardMetaUtils.destroyCardPermanently(c);

        return true;
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class AbstractPlayer_damage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess fieldAccess) throws CannotCompileException {
                    if (fieldAccess.getClassName().equals(AbstractPlayer.class.getName()) &&
                            fieldAccess.getFieldName().equals("lastDamageTaken")) {
                        fieldAccess.replace(String.format("{ if (%1$s.triggerOldBook($0, damageAmount)) { damageAmount = 0; lastDamageTaken = 0; }" +
                                                            "else { $proceed($$); }; } ",
                                OldBookPatch.class.getName()));
                    }
                }
            };
        }
    }
}
