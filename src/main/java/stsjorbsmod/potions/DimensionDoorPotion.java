package stsjorbsmod.potions;

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import stsjorbsmod.JorbsMod;

public class DimensionDoorPotion extends CustomPotion {
    public static final String ID = JorbsMod.makeID(DimensionDoorPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(ID);
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
        super(NAME, ID, PotionRarity.RARE, PotionSize.GHOST, PotionColor.ANCIENT);
        // TODO: Update images and colors with scroll art
        potency = getPotency();
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
