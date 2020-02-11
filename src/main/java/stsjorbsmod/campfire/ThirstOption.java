package stsjorbsmod.campfire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import stsjorbsmod.effects.CampfireThirstEffect;

import static stsjorbsmod.JorbsMod.makeID;

public class ThirstOption extends AbstractCampfireOption {
    public static final String[] TEXT;

    public ThirstOption(boolean active) {
        this.label = TEXT[0];
        this.usable = active;
        this.updateUsability(active);
    }

    public void updateUsability(boolean canUse) {
        this.description = canUse ? TEXT[1] : TEXT[2];
        // TODO: Replace with big boy art
        this.img = ImageMaster.loadImage("images/ui/campfire/ritual.png");
    }

    public void useOption() {
        if (this.usable) {
            AbstractDungeon.effectList.add(new CampfireThirstEffect());
        }
    }

    static {
        TEXT = CardCrawlGame.languagePack.getUIString(makeID("ThirstOption")).TEXT;
    }
}
