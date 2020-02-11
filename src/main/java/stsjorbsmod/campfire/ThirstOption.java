package stsjorbsmod.campfire;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import stsjorbsmod.effects.CampfireThirstEffect;

// TODO:
// 1. Strings need moved to localization
// 2. Button / Art
public class ThirstOption extends AbstractCampfireOption {

    public ThirstOption(boolean active) {
        System.out.println("I was initialized");
        this.label = "Thirst";
        this.usable = active;
        this.updateUsability(active);
    }

    public void updateUsability(boolean canUse) {
        this.description = canUse ? "Heal for Wrath amount on wrathed card" : "No slurp";
        this.img = ImageMaster.loadImage("images/ui/campfire/ritual.png");
    }

    public void useOption() {
        if (this.usable) {
            System.out.println("Thirst was used!");
            AbstractDungeon.effectList.add(new CampfireThirstEffect());
        }
    }
}
