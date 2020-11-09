package stsjorbsmod.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import stsjorbsmod.JorbsMod;

import java.util.function.Function;

import static stsjorbsmod.JorbsMod.makeID;
import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

/**
 * Reduces Burning's falloff rate to 10%
 */
public class AlchemistsFireRelic extends CustomJorbsModIntStatsRelic implements ClickableRelic {
    public static final String ID = JorbsMod.makeID(AlchemistsFireRelic.class);

    public static final String[] ON_CLICK = CardCrawlGame.languagePack.getUIString(makeID("AlchemistsFireOnClick")).TEXT;

    public static final int BURNING_FALLOFF_RATE = 10;
    public static final Function<Integer, Integer> CALCULATE_BURNING_AMOUNT = a -> (a * (100 - BURNING_FALLOFF_RATE)) / 100;

    public AlchemistsFireRelic() {
        super(ID, WANDERER_CARD_COLOR, RelicTier.UNCOMMON, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.flash();
    }

    @Override
    public void onRightClick() {
        AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, ON_CLICK[0], true));
    }
}
