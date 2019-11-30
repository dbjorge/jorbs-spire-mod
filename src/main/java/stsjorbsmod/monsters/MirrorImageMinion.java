package stsjorbsmod.monsters;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
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

    public static final float HITBOX_WIDTH = 134F;
    public static final Float HITBOX_HEIGHT = 280F;

    private static final int MAX_HP = 1;

    private MirrorImagePower owningPower;

    public MirrorImageMinion(MirrorImagePower owningPower) {
        super(strings.NAME,
                ID,
                MAX_HP,
                0F,
                0F,
                HITBOX_WIDTH,
                HITBOX_HEIGHT,
                IMG,
                (owningPower.owner.drawX / Settings.scale) - 1140F,
                18);

        this.owningPower = owningPower;
        IsMonsterFriendlyField.isFriendly.set(this, true);
    }

    @Override
    public void die() {
        super.die();
        AbstractDungeon.actionManager.addToTop(new ReducePowerAction(owningPower.owner, this, owningPower, 1));
    }

    @Override
    public void takeTurn() { }

    @Override
    protected void getMove(int i) { }
}
