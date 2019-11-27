package stsjorbsmod.monsters;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import kobting.friendlyminions.monsters.AbstractFriendlyMonster;
import stsjorbsmod.JorbsMod;

import static stsjorbsmod.JorbsMod.makeMonsterPath;

public class MirrorImageMinion extends AbstractFriendlyMonster {
    public static final String ID = JorbsMod.makeID(MirrorImageMinion.class);
    private static MonsterStrings strings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static String IMG = makeMonsterPath("MirrorImageMinion.png");

    public static final float HITBOX_WIDTH = 134F;
    public static final Float HITBOX_HEIGHT = 280F;

    private static final int MAX_HP = 1;

    public MirrorImageMinion(float offsetX, float offsetY) {
        super(strings.NAME, ID, MAX_HP, 0F, 0F, HITBOX_WIDTH, HITBOX_HEIGHT, IMG, offsetX, offsetY);
    }
}
