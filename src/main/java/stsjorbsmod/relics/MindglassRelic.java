package stsjorbsmod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.PostUpdateSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PenNibPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.OnModifyMemoriesListener;
import stsjorbsmod.powers.MindGlassPower;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.*;

/**
 * When gaining a unique clarity, deals 5 damage to all enemies.
 * When gain the tenth clarity in a combat, deal 500 damage to all enemies.
 */
public class MindglassRelic extends CustomRelic implements OnModifyMemoriesListener, PostUpdateSubscriber {
    public static final String ID = JorbsMod.makeID(MindglassRelic.class.getSimpleName());

    // TODO: relic path
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int ONE_CLARITY_DAMAGE = 5;
    private static final int TEN_CLARITY_DAMAGE = 50; //TODO 500

    public MindglassRelic() {
        super(ID, IMG, OUTLINE, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        BaseMod.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onModifyMemories() {
        JorbsMod.logger.info("I wish :(");
    }

    @Override
    public void onTrigger() {
        ++this.counter;
        this.flash();
        if (this.counter == 10) {
            this.stopPulse();
        } else if (this.counter == 9) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player,
                            new MindGlassPower(AbstractDungeon.player, TEN_CLARITY_DAMAGE),
                            1,
                            true));
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                        (AbstractCreature) null,
                        DamageInfo.createDamageMatrix(ONE_CLARITY_DAMAGE, true),
                        DamageInfo.DamageType.NORMAL,
                        // TODO: More impactful and relevant FX. See FlashAtkImgEffect.loadImage() and
                        //  FlashAtkImgEffect.playSound() for usage of AttackEffect in base game.
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
        BaseMod.unsubscribe(this);
    }

    @Override
    public void initializeTips() {
        this.description = DESCRIPTIONS[0];
        super.initializeTips();
        this.description = getUpdatedDescription();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0].replaceAll(JorbsMod.MOD_ID + ":", "#y");
    }

    @Override
    public void receivePostUpdate() {
        if (this.counter == 9 && !pulse && flashTimer == 0) {
            // TODO: make not interrupt flash()
            //  Currently, it seems like it's not possible to both pulse and flash in the same game tick.
            this.beginLongPulse();
        }
    }
}
