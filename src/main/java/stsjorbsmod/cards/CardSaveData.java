package stsjorbsmod.cards;

import basemod.abstracts.CustomSavableRaw;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.WrathMemory;
import stsjorbsmod.patches.WrathField;

import java.util.ArrayList;

public class CardSaveData implements CustomSavableRaw {
    static class CardData {
        Integer wrathEffectCount;

        CardData(Integer wrathEffectCount) {
            this.wrathEffectCount = wrathEffectCount;
        }
    };
    private ArrayList<CardData> cardData = null;
    private Gson saveFileGson = new Gson();

    @Override
    public JsonElement onSaveRaw() {
        cardData = new ArrayList<>();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            cardData.add(new CardData(WrathField.wrathEffectCount.get(card)));
        }
        return saveFileGson.toJsonTree(cardData);
    }

    @Override
    public void onLoadRaw(JsonElement jsonElement) {
        try {
            cardData = saveFileGson.fromJson(jsonElement, new TypeToken<ArrayList<CardData>>(){}.getType());
        } catch (JsonSyntaxException e) {
            cardData = null;
        }

        final CardGroup masterDeck = AbstractDungeon.player.masterDeck;

        if (jsonElement == null) {
            JorbsMod.logger.warn("CardSaveData found no JSON element to load");
        } else if (cardData == null) {
            JorbsMod.logger.error("CardSaveData failed to parse JSON");
        } else if (cardData.size() != masterDeck.group.size()) {
            JorbsMod.logger.error("CardSaveData has a different number of cards than the master deck");
        } else {
            // Restore meaning to cards from the saved addenda
            int i = 0;

            for (AbstractCard card : masterDeck.group) {
                int wrathEffectCount = cardData.get(i++).wrathEffectCount;
                WrathMemory.reapplyToLoadedCard(card, wrathEffectCount);
            }
        }
    }
}
