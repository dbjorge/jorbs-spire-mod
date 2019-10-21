package stsjorbsmod.variables;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import stsjorbsmod.cards.CustomJorbsModCard;

import static stsjorbsmod.JorbsMod.makeID;

// A second "magic" number for highlighting and display on cards, with possibly different upgraded values.
public class MetaMagicNumber extends DynamicVariable {

    // Reference as "!stsjorbsmod:MetaMagic!" in card strings to actually display the number.
    @Override
    public String key() {
        return makeID("MetaMagic");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return ((CustomJorbsModCard) card).isMetaMagicNumberModified;
    }

    @Override
    public int value(AbstractCard card) {
        return ((CustomJorbsModCard) card).metaMagicNumber;
    }

    @Override
    public int baseValue(AbstractCard card) {
        return ((CustomJorbsModCard) card).baseMetaMagicNumber;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return ((CustomJorbsModCard) card).upgradedMetaMagicNumber;
    }
}
