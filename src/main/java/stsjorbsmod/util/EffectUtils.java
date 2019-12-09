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
        AbstractCard card = cardToShowForVfx.makeStatEquivalentCopy();
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card));
        AbstractDungeon.topLevelEffects.add(new GradeChangeShineEffect(
                (float) Settings.WIDTH / 2.0F,
                (float) Settings.HEIGHT / 2.0F,
                0.6F,
                () -> CardCrawlGame.sound.playAV("CARD_BURN", -0.5F, 2.0F),
                () -> getWrathEffect(card),
                null));
    }

    public static AbstractGameEffect getWrathEffect(AbstractCard card) {
        AbstractGameEffect effect = new FireBurstParticleEffect(card.current_x + (WRATH_TEXT_OFFSET_X + MathUtils.random(-10.0F, 10.0F)) * card.drawScale * Settings.scale,
                card.current_y + (WRATH_TEXT_OFFSET_Y + MathUtils.random(-10.0F, 10.0F)) * card.drawScale * Settings.scale);
        ReflectionUtils.setPrivateField(effect, AbstractGameEffect.class, "color", Color.ORANGE);
        return effect;
    }

    public static void addDowngradeEffect(AbstractCard card, float duration) {
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
        float x = (float) Settings.WIDTH / 2.0F;
        float y = (float) Settings.HEIGHT / 2.0F;
        AbstractDungeon.effectsQueue.add(new GradeChangeShineEffect(
                x,
                y,
                duration,
                () -> CardCrawlGame.sound.playAV("SCENE_TORCH_EXTINGUISH", -0.5F, 7.0F),
                () -> new UpgradeShineParticleEffect(x + MathUtils.random(-10.0F, 10.0F) * Settings.scale, y + MathUtils.random(-10.0F, 10.0F) * Settings.scale),
                Color.FIREBRICK));
    }
}
