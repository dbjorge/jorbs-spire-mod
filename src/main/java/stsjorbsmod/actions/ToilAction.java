package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.ArrayList;

public class ToilAction extends AbstractGameAction {
    private DamageInfo info;
    private AbstractCard theCard = null;

    public ToilAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        setValues(target, info);
        actionType = AbstractGameAction.ActionType.DAMAGE;
        duration = Settings.ACTION_DUR_MED;
    }

    public void update() {
        if (duration == Settings.ACTION_DUR_MED && target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.BLUNT_HEAVY));
            target.damage(info);
            if ((target.isDying || target.currentHealth <= 0) && !target.halfDead) {
                ArrayList<AbstractCard> possibleCards = new ArrayList<>();

                for(AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                    if (c.canUpgrade()) {
                        possibleCards.add(c);
                    }
                }

                if (!possibleCards.isEmpty()) {
                    theCard = possibleCards.get(AbstractDungeon.miscRng.random(0, possibleCards.size() - 1));
                    theCard.upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(theCard);
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        tickDuration();
        if (isDone && theCard != null) {
            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(theCard.makeStatEquivalentCopy()));
            AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
        }

    }
}
