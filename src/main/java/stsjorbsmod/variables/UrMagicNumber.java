package stsjorbsmod.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import stsjorbsmod.cards.CustomJorbsModCard;

import static stsjorbsmod.JorbsMod.makeID;

// A second "magic" number for highlighting and display on cards, with possibly different upgraded values.
public class UrMagicNumber extends DynamicVariable {

    // Reference as "!stsjorbsmod:UrMagic!" in card strings to actually display the number.
    @Override
    public String key() {
        return makeID("UrMagic");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((CustomJorbsModCard) card).isUrMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((CustomJorbsModCard) card).urMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((CustomJorbsModCard) card).baseUrMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((CustomJorbsModCard) card).upgradedUrMagicNumber;
    }
}
