package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;
import stsjorbsmod.memories.SnapCounter;

import static stsjorbsmod.actions.SnapAction.ENEMY_DAMAGE_PER_CLARITY;
import static stsjorbsmod.actions.SnapAction.PLAYER_DAMAGE_PER_CLARITY;
import static stsjorbsmod.memories.SnapCounter.SNAP_TURN;
import static stsjorbsmod.patches.EnumsPatch.SPECIAL;

public class FragilePower extends CustomJorbsModPower implements OnModifyMemoriesSubscriber, HealthBarRenderPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(FragilePower.class);
    public static final String POWER_ID = STATIC.ID;

    private SnapCounter snapCounter;
    private int clarityCount;

    public FragilePower(final AbstractCreature owner, SnapCounter snapCounter) {
        super(STATIC);

        this.owner = owner;
        this.type = SPECIAL;

        this.snapCounter = snapCounter;
        clarityCount = MemoryManager.forPlayer(owner).countCurrentClarities();

        updateDescription();
    }

    public void forceSnapTurn() {
        flash();
        snapCounter.forceSnapTurn();
        updateDescription();
    }

    @Override
    public void onGainClarity(String memoryID) {
        clarityCount = MemoryManager.forPlayer(owner).countCurrentClarities();
        updateDescription();
    }

    @Override
    public void onLoseClarity(String memoryID) {
        clarityCount = MemoryManager.forPlayer(owner).countCurrentClarities();
        updateDescription();
    }

    @Override
    public void onSnap() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (snapCounter.isSnapTurn()) {
            flash();
            addToBot(new SnapAction(owner, true));
        }
    }

    @Override
    public void updateDescription() {
        description = String.format(snapCounter.isSnapTurn() ? DESCRIPTIONS[0] : DESCRIPTIONS[1], SNAP_TURN, clarityCount * PLAYER_DAMAGE_PER_CLARITY, clarityCount * ENEMY_DAMAGE_PER_CLARITY);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FragilePower(owner, snapCounter);
    }

    @Override
    public int getHealthBarAmount() {
        if (!snapCounter.isSnapTurn()) {
            return 0;
        }
        int selfDamage = clarityCount * PLAYER_DAMAGE_PER_CLARITY;
        if (owner.hasPower(IntangiblePlayerPower.POWER_ID)) {
            selfDamage = MathUtils.clamp(selfDamage, 0, 1);
        }
        return selfDamage;
    }

    @Override
    public Color getColor() {
        return new Color(
                (MathUtils.cosDeg((float) (System.currentTimeMillis() / 10L % 360L)) + 1.25F) / 2.3F,
                (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 1000L) / 10L % 360L)) + 1.25F) / 2.3F,
                (MathUtils.cosDeg((float) ((System.currentTimeMillis() + 2000L) / 10L % 360L)) + 1.25F) / 2.3F,
                1.0F);
    }
}