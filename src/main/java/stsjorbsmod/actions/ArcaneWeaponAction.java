package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.random.Random;
import stsjorbsmod.effects.ArcaneWeaponEffect;
import stsjorbsmod.powers.BanishedPower;
import stsjorbsmod.util.CombatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArcaneWeaponAction extends AbstractGameAction {
    private AbstractCard card;

    public ArcaneWeaponAction(AbstractCard card) {
        this.card = card;
    }

    @Override
    public void update() {
        target = CombatUtils.getRandomMonster(AbstractDungeon.getMonsters(),
                AbstractDungeon.getMonsters().monsters.stream().filter(m -> m.hasPower(BanishedPower.POWER_ID)).collect(Collectors.toList()),
                true,
                AbstractDungeon.cardRandomRng);
        if (this.target != null) {
            card.calculateCardDamage((AbstractMonster) target);

            // Add to top in reverse order so VFX starts/waits before the damage effect happens.
            AbstractDungeon.actionManager.addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, card.damage, card.damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
            AbstractDungeon.actionManager.addToTop(new VFXAction(new ArcaneWeaponEffect(target.hb.x, target.hb.cY + target.hb.height / 12.0F), 0.5F));
        }

        isDone = true;
    }
}
