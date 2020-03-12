package stsjorbsmod.relics;

import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;

import java.util.ArrayList;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

public class BookOfTrialsRelic extends CustomJorbsModRelic {
    public static final String ID = JorbsMod.makeID(AlchemistsFireRelic.class);

    public BookOfTrialsRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
    }
}
