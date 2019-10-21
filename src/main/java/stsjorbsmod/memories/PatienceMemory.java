package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.CoilPower;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

// Gain 1 Coil each time you play a card. When forgetting, deal 2 damage to all enemies for each Coil and lose all Coil.
public class PatienceMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(PatienceMemory.class);

    private static final int COIL_PER_CARD = 1;
    public static final int DAMAGE_PER_COIL_ON_LEAVE = 2;

    public PatienceMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.VIRTUE, owner, isClarified);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster target) {
        if (isPassiveEffectActive) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(owner, owner, new CoilPower(owner, owner, COIL_PER_CARD)));
        }
    }

    @Override
    public void onForget() {
        if (!owner.hasPower(CoilPower.POWER_ID)) {
            return;
        }

        this.flash();

        int numCoil = owner.getPower(CoilPower.POWER_ID).amount;
        int enemyDamage = DAMAGE_PER_COIL_ON_LEAVE * numCoil;

        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(enemyDamage, true), DamageType.THORNS, AttackEffect.FIRE));
        AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(owner, owner, CoilPower.POWER_ID));
    }
}
