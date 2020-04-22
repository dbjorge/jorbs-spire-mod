package stsjorbsmod.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.cull.deckoftrials.DeckOfTrials;
import stsjorbsmod.patches.AbstractPlayerClassPatch;

import java.util.ArrayList;

import static stsjorbsmod.characters.Cull.Enums.CULL_CARD_COLOR;

public class BookOfTrialsRelic extends CustomJorbsModRelic implements AtStartOfActRelicSubscriber {
    public static final String ID = JorbsMod.makeID(BookOfTrialsRelic.class);

    public BookOfTrialsRelic() {
        super(ID, CULL_CARD_COLOR, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atStartOfAct() {
        AbstractDungeon.player.getRelic(BookOfTrialsRelic.ID).flash();
        ArrayList<AbstractCard> cards = DeckOfTrials.drawCards(2);
        for (AbstractCard card : cards) {
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(card, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }
    }
}
