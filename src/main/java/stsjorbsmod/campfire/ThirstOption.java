package stsjorbsmod.campfire;

import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class ThirstOption extends AbstractCampfireOption {

    public ThirstOption(boolean active) {
        System.out.println("I was initialized");
        this.label = "Thirst";
        this.usable = active;
        this.updateUsability(active);
    }

    public void updateUsability(boolean canUse) {
        this.description = canUse ? "Heal for Wrath amount on wrathed card" : "No slurp";
    }

    public void useOption() {
        if (this.usable) {
            System.out.println("Thirst was used!");
        }
    }
}
