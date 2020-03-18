package stsjorbsmod.relics;

import stsjorbsmod.JorbsMod;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

public class BookOfTrialsRelic extends CustomJorbsModRelic {
    public static final String ID = JorbsMod.makeID(BookOfTrialsRelic.class);

    public BookOfTrialsRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
    }
}
