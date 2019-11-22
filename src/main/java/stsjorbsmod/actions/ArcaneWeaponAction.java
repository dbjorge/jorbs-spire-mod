package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.effects.ArcaneWeaponEffect;

public class ArcaneWeaponAction extends AbstractGameAction {
    private AbstractCard card;

    public ArcaneWeaponAction(AbstractCard card) {
        this.card = card;
    }

    /**
     * Add to top in reverse order so this action takes precedent.
     */
    @Override
    public void update() {
        target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster) null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            card.calculateCardDamage((AbstractMonster) target);
            AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.5F));
            AbstractDungeon.actionManager.addToTop(new VFXAction(new ArcaneWeaponEffect(target.hb.x, target.hb.cY + target.hb.height / 12.0F)));
        }

        isDone = true;
    }
}
