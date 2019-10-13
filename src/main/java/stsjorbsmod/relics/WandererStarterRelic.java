package stsjorbsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.powers.PatienceMemoryPower;
import stsjorbsmod.util.MemoryPowerUtils;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makeRelicOutlinePath;
import static stsjorbsmod.JorbsMod.makeRelicPath;

// Start each fight remembering Patience. At the end of each fight, gain 1hp per Clarity.
public class WandererStarterRelic extends CustomRelic {
    private static final int HEAL_PER_CLARITY = 1;

    public static final String ID = JorbsMod.makeID(WandererStarterRelic.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("wanderer_starter_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("wanderer_starter_relic.png"));

    public WandererStarterRelic() {
        super(ID, IMG, OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
        AbstractPlayer player = AbstractDungeon.player;
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RememberSpecificMemoryAction(player, player, new PatienceMemoryPower(player, player)));
    }

    @Override
    public void atTurnStart() {
        ++this.counter;
        if (this.counter == 7) {
            this.beginLongPulse();
        }
    }

    @Override
    public void onPlayerEndTurn() {
        if (this.counter == 7) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new SnapAction(AbstractDungeon.player));
            this.stopPulse();
        }
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();

        this.flash();
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractPlayer p = AbstractDungeon.player;

        if (p.currentHealth > 0) {
            p.heal(MemoryPowerUtils.countClarities(p) * HEAL_PER_CLARITY);
        }
    }

    @Override
    public void initializeTips() {
        this.description = DESCRIPTIONS[0];
        super.initializeTips();
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replaceAll(JorbsMod.getModID() + ":", "#y");
    }
}
