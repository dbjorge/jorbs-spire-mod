package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import stsjorbsmod.util.IntentUtils;

public class FlameWardPower extends CustomJorbsModPower implements OnDamagedPreBlockSubscriber {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(FlameWardPower.class);
    public static final String POWER_ID = STATIC.ID;

    public FlameWardPower(AbstractPlayer owner, int blockAmount, int burningAmount) {
        super(STATIC);

        this.type = PowerType.BUFF;

        this.owner = owner;
        this.amount = blockAmount;
        this.amount2 = burningAmount;
        this.isTurnBased = true;

        this.updateDescription();
    }

    @Override
    public void onDamagedPreBlock(DamageInfo info) {
        if (info != null && this.owner != info.owner && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.owner.hb.cX, this.owner.hb.cY, AbstractGameAction.AttackEffect.SHIELD));
            AbstractDungeon.effectList.add(new FlameBarrierEffect(this.owner.hb.cX, this.owner.hb.cY));
            this.owner.addBlock(this.amount);
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                AbstractDungeon.actionManager.addToTop(
                        new ApplyPowerAction(m, this.owner, new BurningPower(m, this.owner, this.amount2), this.amount2, AbstractGameAction.AttackEffect.FIRE));
            }
            this.flash();
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, this.amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount2, this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FlameWardPower((AbstractPlayer) owner, amount, amount2);
    }
}
