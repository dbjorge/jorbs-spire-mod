package stsjorbsmod.relics;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;

import java.util.ArrayList;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

public class BookOfTrialsRelic extends CustomJorbsModRelic implements AtStartOfActRelicSubscriber, HasRelicStats {
    public static final String ID = JorbsMod.makeID(BookOfTrialsRelic.class);
    private static ArrayList<String> statsCards;  // Relic Stats needs these to be static
    private static String savedText;

    public BookOfTrialsRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
        statsCards = new ArrayList<>();
    }

    @Override
    public void atStartOfAct() {
        AbstractDungeon.player.getRelic(BookOfTrialsRelic.ID).flash();
        ArrayList<AbstractCard> cards = DeckOfTrials.drawCards(2);
        for (AbstractCard card : cards) {
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            statsCards.add(card.cardID);
        }
        updateStatsDescription();
    }

    @Override
    public String getStatsDescription() {
        return savedText;
    }

    @Override
    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    private void updateStatsDescription() {
        StringBuilder statText = new StringBuilder(DESCRIPTIONS[DESCRIPTIONS.length - 1]);
        for (String cardId: statsCards) {
            statText.append(" NL ");
            if (CardLibrary.isACard(cardId)) {
                AbstractCard card = CardLibrary.getCard(cardId);
                statText.append(card.name);
            } else {
                statText.append(cardId);
            }
        }
        savedText = statText.toString();
    }

    @Override
    public void resetStats() {
        if (statsCards == null) {
            statsCards = new ArrayList<>();
        } else {
            statsCards.clear();
        }
        updateStatsDescription();
    }

    @Override
    public JsonElement onSaveStats() {
        return new Gson().toJsonTree(statsCards);
    }

    @Override
    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            statsCards = new ArrayList<>();
            for (JsonElement e : jsonArray) {
                statsCards.add(e.getAsString());
            }
            updateStatsDescription();
        } else {
            resetStats();
        }
    }

}
