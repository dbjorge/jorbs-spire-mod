package net.dbjorge.stsjorbsmod.characters;

import java.util.ArrayList;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.devcommands.relic.RelicList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.cards.blue.Claw;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.Boot;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import net.dbjorge.stsjorbsmod.Assets;
import net.dbjorge.stsjorbsmod.JorbsMod;
import net.dbjorge.stsjorbsmod.patches.AbstractCardEnum;
import net.dbjorge.stsjorbsmod.patches.WandererPlayerClassEnum;

public class WandererCharacter extends CustomPlayer {
    public static final Color COLOR = CardHelper.getColor(100.0f, 80.0f, 110.0f);
    public static final int ENERGY_PER_TURN = 3; // how much energy you get every turn
    public static final String MY_CHARACTER_SHOULDER_2 = "img/char/shoulder2.png"; // campfire pose
    public static final String MY_CHARACTER_SHOULDER_1 = "img/char/shoulder1.png"; // another campfire pose
    public static final String MY_CHARACTER_CORPSE = "img/char/corpse.png"; // dead corpse
    public static final String MY_CHARACTER_SKELETON_ATLAS = "img/char/skeleton.atlas"; // spine animation atlas
    public static final String MY_CHARACTER_SKELETON_JSON = "img/char/skeleton.json"; // spine animation json

    public static final int STARTING_HP = 69;
    public static final int MAX_HP = 69;
    public static final int STARTING_GOLD = 99;
    public static final int HAND_SIZE = 5;
    public static final int ORB_SLOTS = 0;

    public WandererCharacter (String name) {
        super(name, WandererPlayerClassEnum.WANDERER_PLAYER_CLASS);

        // set location for text bubbles
        this.dialogX = (this.drawX + 0.0F * Settings.scale);
        this.dialogY = (this.drawY + 220.0F * Settings.scale);

        // required call to load textures and setup energy/loadout
        initializeClass(
                null,
                MY_CHARACTER_SHOULDER_2,
                MY_CHARACTER_SHOULDER_1,
                MY_CHARACTER_CORPSE,
                getLoadout(),
                20.0F,
                -10.0F,
                220.0F,
                290.0F,
                new EnergyManager(ENERGY_PER_TURN));

        // if you're using modified versions of base game animations or made animations in spine make sure to include this bit and the following lines
        loadAnimation(MY_CHARACTER_SKELETON_ATLAS, MY_CHARACTER_SKELETON_JSON, 1.0F);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "animation", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public static void registerColor() {
        JorbsMod.logger.info("Registering Wanderer color");

        BaseMod.addColor(
                AbstractCardEnum.WANDERER,
                WandererCharacter.COLOR,
                WandererCharacter.COLOR,
                WandererCharacter.COLOR,
                WandererCharacter.COLOR,
                WandererCharacter.COLOR,
                WandererCharacter.COLOR,
                WandererCharacter.COLOR,
                "images/wanderer/card_bg_attack_512.png",
                "images/wanderer/card_bg_skill_512.png",
                "images/wanderer/card_bg_power_512.png",
                "images/wanderer/card_bg_energy_orb_512.png",
                "images/wanderer/card_bg_attack_1024.png",
                "images/wanderer/card_bg_skill_1024.png",
                "images/wanderer/card_bg_power_1024.png",
                "images/wanderer/card_bg_energy_orb_1024.png",
                "images/wanderer/energy_orb_164.png");
    }

    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for(int i=0;i<10;i++) {
            retVal.add(Claw.ID);
        }
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Boot.ID);
        UnlockTracker.markRelicAsSeen(Boot.ID);
        return retVal;
    }

    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                "The Wanderer",
                "Wizard w/ advancing and aggressive memory loss due to constant spire resurrection",
                STARTING_HP,
                MAX_HP,
                ORB_SLOTS,
                STARTING_GOLD,
                HAND_SIZE,
                this,
                getStartingRelics(),
                getStartingDeck(),
                false);
    }

}