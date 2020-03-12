package stsjorbsmod.relics;

import stsjorbsmod.JorbsMod;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

public class BookOfToilsRelic extends CustomJorbsModRelic {
    public static final String ID = JorbsMod.makeID(BookOfToilsRelic.class);

    public BookOfToilsRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
    }
}
