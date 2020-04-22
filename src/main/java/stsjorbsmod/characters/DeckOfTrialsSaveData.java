package stsjorbsmod.characters;

import basemod.abstracts.CustomSavable;
import basemod.devcommands.deck.Deck;
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
        String str = String.join(",", DeckOfTrials.deck.group.stream().map(c -> c.cardID).collect(Collectors.toList()));
        System.out.println(str);
        return str;
    }

    @Override
    public void onLoad(String deckOfTrials) {
        System.out.println(deckOfTrials);
        Arrays.asList(deckOfTrials.split(",")).forEach(c -> DeckOfTrials.deck.addToTop(CardLibrary.getCard(CULL, c)));
    }
}
