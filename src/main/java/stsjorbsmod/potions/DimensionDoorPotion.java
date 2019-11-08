package stsjorbsmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FruitJuice;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.ReflectionUtils;

// Extending FruitJuice is a hack that causes the game to allow use during non-combat screens
public class DimensionDoorPotion extends FruitJuice {
    public static final String POTION_ID = JorbsMod.makeID(DimensionDoorPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    private static boolean isMapTravelActive = false;

    public static boolean isMapTravelActive() {
        return isMapTravelActive;
    }

    public static int getMapTravelRange() {
        return 0;
    }

    private static void activateMapTravel() {
        isMapTravelActive = true;
    }

    public static void deactivateMapTravel() {
        isMapTravelActive = false;
    }

    public DimensionDoorPotion() {
        super();
        this.ID = POTION_ID;
        this.name = NAME;
        this.description = DESCRIPTIONS[0];
        this.rarity = PotionRarity.RARE;
        this.size = PotionSize.GHOST;
        this.potency = getPotency();

        // Replicating some of AbstractPotion.ctor to enable use of "extends FruitPotion" hack
        // TODO: use scroll-shaped art instead
        ReflectionUtils.setPrivateField(this, AbstractPotion.class, "containerImg", ImageMaster.POTION_CARD_CONTAINER);
        ReflectionUtils.setPrivateField(this, AbstractPotion.class, "liquidImg", ImageMaster.POTION_CARD_LIQUID);
        ReflectionUtils.setPrivateField(this, AbstractPotion.class, "hybridImg", ImageMaster.POTION_CARD_HYBRID);
        ReflectionUtils.setPrivateField(this, AbstractPotion.class, "spotsImg", ImageMaster.POTION_CARD_SPOTS);
        ReflectionUtils.setPrivateField(this, AbstractPotion.class, "outlineImg", ImageMaster.POTION_CARD_OUTLINE);
        this.liquidColor = Color.TAN.cpy();
        this.hybridColor = Color.TAN.cpy();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature abstractCreature) {
        activateMapTravel();
    }

    @Override
    public boolean canUse() {
        // Only one scroll may be used for active effect. Others must be discarded if the
        // player wishes to be rid of them.
        return !isMapTravelActive();
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new DimensionDoorPotion();
    }
}
