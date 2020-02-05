package stsjorbsmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.IncreaseManifestAction;

public class GhostPoisonPotion extends AbstractPotion {
    public static final String POTION_ID = JorbsMod.makeID(GhostPoisonPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    private static final int BASE_POTENCY = 1;
    private static final int MANIFEST_PER_ENEMY = 1;

    public GhostPoisonPotion() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.H, PotionColor.SMOKE);
        this.potency = this.getPotency();
        this.description = String.format(DESCRIPTIONS[0], MANIFEST_PER_ENEMY);
        this.isThrown = true;
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle("stsjorbsmod:manifest"), BaseMod.getKeywordDescription("stsjorbsmod:manifest")));
    }

    @Override
    public void use(AbstractCreature target) {
        int liveMonsterCount = 0;

        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            // This makes sure that all Darklings in the fight actually die, regardless of their halfDead state
            if (m instanceof Darkling) {
                if (m.halfDead) {
                    m.halfDead = false;
                }
                // This makes combat against Darklings able to end
                AbstractDungeon.getCurrRoom().cannotLose = false;
            }
            if (!m.isDeadOrEscaped()) {
                ++liveMonsterCount;
                addToBot(new VFXAction(new SmokeBombEffect(m.hb.cX, m.hb.cY)));
                m.die();
            }
        }
            addToBot(new IncreaseManifestAction(liveMonsterCount));

        if ((AbstractDungeon.getCurrRoom()).phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractDungeon.player.hideHealthBar();
            AbstractDungeon.overlayMenu.endTurnButton.disable();
        }
    }

    @Override
    public boolean canUse() {
        if (super.canUse()) {
            for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (m.type == AbstractMonster.EnemyType.BOSS || m.type == AbstractMonster.EnemyType.ELITE)
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_POTENCY;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new GhostPoisonPotion();
    }
}
