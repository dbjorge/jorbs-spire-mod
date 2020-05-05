package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.ExertedField;
import stsjorbsmod.util.CombatUtils;

public class RepressedMemoryAction extends AbstractGameAction {
    private float baseDuration;
    private AbstractCard card;

    public RepressedMemoryAction(AbstractCard card) {
        this.duration = baseDuration = Settings.ACTION_DUR_FAST;
        this.card = card;
    }

    @Override
    public void update() {
        //if (duration == baseDuration) {
          //  AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.BLUNT_HEAVY));
       // }
        //tickDuration();
        //if (isDone) {
            if (CombatUtils.isCombatBasicallyVictory()) {
                addToBot(new ExertAction(this.card));
                JorbsMod.logger.info("Did ExertAction. Exerted? " + ExertedField.exerted.get(this.card));
                isDone = true;
            }
        //}
    }
}
