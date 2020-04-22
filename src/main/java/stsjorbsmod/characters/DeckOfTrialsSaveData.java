package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;

import java.util.Arrays;
import java.util.stream.Collectors;

import static stsjorbsmod.characters.Cull.Enums.CULL;

public class DeckOfTrialsSaveData implements CustomSavable<String> {
    @Override
    public String onSave() {
        return String.join(",", DeckOfTrials.deck.group.stream().map(c -> c.cardID).collect(Collectors.toList()));
    }

    @Override
    public void onLoad(String deckOfTrials) {
        Arrays.asList(deckOfTrials.split(",")).forEach(c -> DeckOfTrials.deck.addToTop(CardLibrary.getCard(CULL, c)));
    }
}
