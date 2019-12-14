package stsjorbsmod.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.UpgradeShineParticleEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import stsjorbsmod.effects.GradeChangeShineEffect;

import static stsjorbsmod.patches.WrathCardIconPatch.AbstractCard_renderEnergy.WRATH_TEXT_OFFSET_X;
import static stsjorbsmod.patches.WrathCardIconPatch.AbstractCard_renderEnergy.WRATH_TEXT_OFFSET_Y;

public class EffectUtils {
    public static void addPermanentCardUpgradeEffect(AbstractCard cardToShowForVfx) {
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(cardToShowForVfx.makeStatEquivalentCopy()));
        AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
    }

    public static void addWrathCardUpgradeEffect(AbstractCard cardToShowForVfx) {
        if (!cardToShowForVfx.purgeOnUse) {
            AbstractCard card = cardToShowForVfx.makeStatEquivalentCopy();
            ShowCardBrieflyEffect showCardBrieflyEffect = new ShowCardBrieflyEffect(card);
            float duration = Settings.FAST_MODE ? Settings.ACTION_DUR_XLONG : showCardBrieflyEffect.startingDuration;
            showCardBrieflyEffect.duration = showCardBrieflyEffect.startingDuration = duration;
            AbstractDungeon.topLevelEffects.add(showCardBrieflyEffect);
            AbstractDungeon.topLevelEffects.add(new GradeChangeShineEffect(
                    (float) Settings.WIDTH / 2.0F,
                    (float) Settings.HEIGHT / 2.0F,
                    Settings.ACTION_DUR_MED,
                    () -> CardCrawlGame.sound.playAV("CARD_BURN", -0.5F, 2.0F),
                    () -> getWrathEffect(card),
                    null,
                    false));
            AbstractDungeon.actionManager.addToTop(new WaitAction(duration));
        }
    }

    public static AbstractGameEffect getWrathEffect(AbstractCard card) {
        AbstractGameEffect effect = new FireBurstParticleEffect(card.current_x + (WRATH_TEXT_OFFSET_X + MathUtils.random(-10.0F, 10.0F)) * card.drawScale * Settings.scale,
                card.current_y + (WRATH_TEXT_OFFSET_Y + MathUtils.random(-10.0F, 10.0F)) * card.drawScale * Settings.scale);
        ReflectionUtils.setPrivateField(effect, AbstractGameEffect.class, "color", Color.ORANGE);
        return effect;
    }

    public static void showDowngradeEffect(AbstractCard card) {
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
        float x = (float) Settings.WIDTH / 2.0F;
        float y = (float) Settings.HEIGHT / 2.0F;
        AbstractDungeon.topLevelEffects.add(new GradeChangeShineEffect(
                x,
                y,
                Settings.ACTION_DUR_LONG,
                () -> CardCrawlGame.sound.playAV("SCENE_TORCH_EXTINGUISH", -0.5F, 10.0F),
                // TODO: this effect needs to change to something more negative, but figuring out this many particles is a bit out of my brain capacity ATM.
                // TODO: somehow include the hammer effect too.
                () -> new UpgradeShineParticleEffect(x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, y + MathUtils.random(-10.0F, 10.0F) * Settings.scale),
                Color.FIREBRICK,
                true));
        AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_LONG));
    }

    public static void showDestroyEffect(AbstractCard card) {
        CardCrawlGame.sound.play("CARD_EXHAUST");
        PurgeCardEffect purgeCardEffect = new PurgeCardEffect(card, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2));
        float duration = Settings.FAST_MODE ? 0.75F : purgeCardEffect.startingDuration;
        purgeCardEffect.duration = purgeCardEffect.startingDuration = duration;
        AbstractDungeon.topLevelEffects.add(purgeCardEffect);
        AbstractDungeon.actionManager.addToTop(new WaitAction(duration));
    }
}
