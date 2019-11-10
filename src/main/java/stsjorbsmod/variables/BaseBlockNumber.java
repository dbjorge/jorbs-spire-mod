package stsjorbsmod.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;

import static stsjorbsmod.JorbsMod.makeID;

// For use with cards like "Gain !B! Block (!stsjorbsmod:BaseBlock! + !M! block per clarity)".
// Should generally only be used *in addition to* !B!, not *instead of* !B!.
public class BaseBlockNumber extends DynamicVariable {
    // Reference as "!stsjorbsmod:BaseBlock!" in card strings to actually display the number.
    @Override
    public String key() {
        return makeID("BaseBlock");
    }

    @Override
    public boolean isModified(AbstractCard card) { return false; }

    @Override
    public int value(AbstractCard card) {
        return card.baseBlock;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return card.baseBlock;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return false;
    }
}
