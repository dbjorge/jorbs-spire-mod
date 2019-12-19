package stsjorbsmod.monsters;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.IsMonsterFriendlyField;
import stsjorbsmod.powers.MirrorImagePower;

import static stsjorbsmod.JorbsMod.makeMonsterPath;

public class MirrorImageMinion extends AbstractMonster {
    public static final String ID = JorbsMod.makeID(MirrorImageMinion.class);
    private static MonsterStrings strings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static String IMG = makeMonsterPath("MirrorImageMinion.png");

    public static final float WIDTH = 67F;
    public static final float HEIGHT = 140F;
    public static final float OWNER_OFFSET_X = 97F;
    public static final float OWNER_OFFSET_Y = 103F;

    private MirrorImagePower owningPower;

    public MirrorImageMinion(MirrorImagePower owningPower, int maxHP) {
        super(strings.NAME,
                ID,
                maxHP,
                0F,
                0F,
                WIDTH,
                HEIGHT,
                IMG,
                0, // overridden below
                0); // overridden below

        this.owningPower = owningPower;
        IsMonsterFriendlyField.isFriendly.set(this, true);

        setPositionFromOwner(owningPower.owner);
    }

    @Override
    public void update() {
        super.update();
        // The desired effect is for the minion to turn with the player in the shield and spear fight, but
        // stay behind when the player smoke bombs.
        if (!owningPower.owner.isEscaping) {
            setPositionFromOwner(owningPower.owner);
        }
    }

    public void setPositionFromOwner(AbstractCreature owner) {
        this.flipHorizontal = owner.flipHorizontal;
        int flipMultiplier = owner.flipHorizontal ? -1 : 1;
        this.drawX = owner.drawX + flipMultiplier * (OWNER_OFFSET_X * Settings.scale);
        this.drawY = owner.drawY + (OWNER_OFFSET_Y * Settings.scale);
    }

    @Override
    public void damage(DamageInfo info) {
        int oldHealth = this.currentHealth;
        super.damage(info);
        // We expect to be permanently intangible, so this should always be 1 (normal case) or 0 (dark shackles stuff)
        int decrease = oldHealth - this.currentHealth;
        if (decrease > 0) {
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(owningPower.owner, this, owningPower, decrease));
        }
    }
    @Override
    public void die(boolean triggerRelics) {
        super.die(false); // minion death shouldn't trigger relics' onMonsterDeath()
    }

    @Override
    public void takeTurn() { }

    @Override
    protected void getMove(int i) { }
}
