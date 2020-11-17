package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import stsjorbsmod.cards.cull.ReapAndSow;
import stsjorbsmod.patches.ActionShouldPersistPostCombatField;

public class SowAction extends AbstractGameAction {
    private int sowDamage;

    public SowAction(int sowDamage) {
        this.sowDamage = sowDamage;
        ActionShouldPersistPostCombatField.shouldPersistPostCombat.set(this, true);
    }

    public void update() {
        AbstractCard card = new ReapAndSow();
        ShowCardBrieflyEffect showCardBrieflyEffect = new ShowCardBrieflyEffect(card);
        float duration = Settings.FAST_MODE ? Settings.ACTION_DUR_XLONG : showCardBrieflyEffect.startingDuration;
        showCardBrieflyEffect.duration = showCardBrieflyEffect.startingDuration = duration;
        AbstractDungeon.topLevelEffects.add(showCardBrieflyEffect);

        addToBot(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(this.sowDamage, true),
                DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));

        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c instanceof stsjorbsmod.cards.cull.ReapAndSow) {
                c.misc = 0;
            }
        }
        this.isDone = true;
    }
}
