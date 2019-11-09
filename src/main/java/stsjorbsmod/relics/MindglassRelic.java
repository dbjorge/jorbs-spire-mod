package stsjorbsmod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.PostUpdateSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.OnModifyMemoriesListener;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.*;

public class MindglassRelic extends CustomRelic implements OnModifyMemoriesListener, PostUpdateSubscriber {
    public static final String ID = JorbsMod.makeID(MindglassRelic.class.getSimpleName());

    // TODO: relic path
    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("placeholder_relic.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("placeholder_relic.png"));

    private static final int ONE_CLARITY_DAMAGE = 5;
    private static final int TEN_CLARITY_DAMAGE = 500;

    public MindglassRelic() {
        // TODO relic tier
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.CLINK);
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
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAllEnemiesAction(
                            (AbstractCreature) null,
                            DamageInfo.createDamageMatrix(TEN_CLARITY_DAMAGE, true),
                            DamageInfo.DamageType.NORMAL,
                            // TODO: More impactful and relevant FX. See FlashAtkImgEffect.loadImage() and
                            //  FlashAtkImgEffect.playSound() for usage of AttackEffect in base game.
                            AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        } else {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAllEnemiesAction(
                            (AbstractCreature) null,
                            DamageInfo.createDamageMatrix(ONE_CLARITY_DAMAGE, true),
                            DamageInfo.DamageType.NORMAL,
                            // TODO: More impactful and relevant FX. See FlashAtkImgEffect.loadImage() and
                            //  FlashAtkImgEffect.playSound() for usage of AttackEffect in base game.
                            AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
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
