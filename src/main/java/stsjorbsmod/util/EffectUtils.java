package stsjorbsmod.util;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import stsjorbsmod.effects.GradeChangeShineEffect;

public class EffectUtils {
    public static void addPermanentCardUpgradeEffect(AbstractCard cardToShowForVfx) {
        AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(cardToShowForVfx.makeStatEquivalentCopy()));
        AbstractDungeon.topLevelEffects.add(new GradeChangeShineEffect(
                (float) Settings.WIDTH / 2.0F,
                (float) Settings.HEIGHT / 2.0F,
                0.6F,
                () -> CardCrawlGame.sound.playAV("CARD_BURN", -0.5F, 2.0F)
        ));
    }

}
