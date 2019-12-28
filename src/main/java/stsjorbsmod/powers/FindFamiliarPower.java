package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class FindFamiliarPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(FindFamiliarPower.class);
    public static final String POWER_ID = STATIC.ID;

    public FindFamiliarPower(final AbstractCreature owner, final int damageIfSnapped) {
        super(STATIC);

        this.owner = owner;
        this.amount = damageIfSnapped;

        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        MemoryManager mm = MemoryManager.forPlayer(owner);
        if (isPlayer && mm != null) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new GainClarityOfCurrentMemoryAction(owner));

            if (mm.isSnapped() && amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(DESCRIPTIONS[0], amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FindFamiliarPower(owner, amount);
    }
}
