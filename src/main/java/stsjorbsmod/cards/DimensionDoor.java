package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

// 0: Escape from a non-boss combat and receive no rewards. If you escape, you may ignore paths when choosing the next room to travel to. Destroy this card.
public class DimensionDoor extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(DimensionDoor.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Rares/dimension_door.png");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;
    private static final int VERTICAL_RANGE = 0;
    private static final int UPGRADE_PLUS_VERTICAL_RANGE = 1;

    private static Integer mapTravelRange = null;

    public static boolean isMapTravelActive() {
        return mapTravelRange != null;
    }

    public static int getMapTravelRange() {
        return mapTravelRange;
    }

    private static void activateMapTravel(int extraRange) {
        mapTravelRange = extraRange;
    }

    public static void deactivateMapTravel() {
        mapTravelRange = null;
    }

    public DimensionDoor() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = VERTICAL_RANGE;
        this.purgeOnUse = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (canUse()) {
            activateMapTravel(magicNumber);

            removeFromMasterDeck();

            // Fleeing sequence copied from SmokeBomb.use()
            AbstractDungeon.getCurrRoom().smoked = true;
            enqueueAction(new VFXAction(new SmokeBombEffect(p.hb.cX, p.hb.cY)));
            p.hideHealthBar();
            p.isEscaping = true;
            p.flipHorizontal = !p.flipHorizontal;
            AbstractDungeon.overlayMenu.endTurnButton.disable();
            p.escapeTimer = 2.5F;
        } else {
            removeFromMasterDeck();

            AbstractDungeon.effectList.add(
                    new ThoughtBubble(p.dialogX, p.dialogY, 3.0F, TEXT[0], true));
        }
    }

    private boolean canUse() {
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream().noneMatch(
                m -> m.type == AbstractMonster.EnemyType.BOSS || m.hasPower(BackAttackPower.POWER_ID)
        );
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VERTICAL_RANGE);
            upgradeDescription();
        }
    }
}
