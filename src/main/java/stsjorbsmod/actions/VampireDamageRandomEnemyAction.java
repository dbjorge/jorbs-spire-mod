package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.powers.BlackTentaclesPower;

public class VampireDamageRandomEnemyAction extends AbstractGameAction {
    private AbstractCard card;
    private AttackEffect effect;

    public VampireDamageRandomEnemyAction(AbstractCard card, AttackEffect effect) {
        this.card = card;
        this.effect = effect;
    }

    public void update() {
        this.target = AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            BlackTentaclesPower.applyPossibleActionTargetOverride(this);
            this.card.calculateCardDamage((AbstractMonster)this.target);
            AbstractDungeon.actionManager.addToTop(new VampireDamageAction(this.target, new DamageInfo(AbstractDungeon.player, this.card.damage, this.card.damageTypeForTurn), this.effect));
        }

        this.isDone = true;
    }
}
