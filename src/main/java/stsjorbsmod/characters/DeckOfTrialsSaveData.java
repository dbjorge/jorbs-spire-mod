package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;
import stsjorbsmod.patches.AbstractPlayerClassPatch;

import java.util.Arrays;
import java.util.stream.Collectors;

import static stsjorbsmod.characters.Cull.Enums.CULL;

public class DeckOfTrialsSaveData implements CustomSavable<String> {
    @Override
    public String onSave() {
        DeckOfTrials deck = AbstractPlayerClassPatch.deckOfTrials.get(AbstractDungeon.player);
        if (deck == null) {
            deck.reset();
        }
        return String.join(",", deck.deck.group.stream().map(c -> c.cardID).collect(Collectors.toList()));
    }

    @Override
    public void onLoad(String deckOfTrials) {
        DeckOfTrials deck = new DeckOfTrials();
        Arrays.asList(deckOfTrials.split(",")).forEach(c -> deck.deck.addToTop(CardLibrary.getCard(CULL, c)));
    }
}
