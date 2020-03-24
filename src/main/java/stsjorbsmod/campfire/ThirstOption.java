package stsjorbsmod.campfire;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import stsjorbsmod.effects.CampfireThirstEffect;

import static stsjorbsmod.JorbsMod.makeID;
import static stsjorbsmod.JorbsMod.makeImagePath;

public class ThirstOption extends AbstractCampfireOption {
    public static final Texture IMG = ImageMaster.loadImage(makeImagePath("ui/campfire_thirst.png"));
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID("ThirstOption")).TEXT;

    public ThirstOption(boolean active) {
        this.label = TEXT[0];
        this.description = active ? TEXT[1] : TEXT[2];
        this.img = IMG;
        this.usable = active;
    }

    @Override
    public void useOption() {
        if (this.usable) {
            AbstractDungeon.effectList.add(new CampfireThirstEffect());
        }
    }
}
