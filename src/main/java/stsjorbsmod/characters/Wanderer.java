package stsjorbsmod.characters;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.wanderer.*;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.relics.FragileMindRelic;
import stsjorbsmod.relics.WandererStarterRelic;

import java.util.ArrayList;
import java.util.List;

import static stsjorbsmod.JorbsMod.*;
import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in JorbsMod-Character-Strings.json in the resources

public class Wanderer extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(JorbsMod.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    // These are enums for your Characters color (both general color and for the card library) as well as
    // an enum for the name of the player class - IRONCLAD, THE_SILENT, DEFECT, YOUR_CLASS ...
    // These are all necessary for creating a character. If you want to find out where and how exactly they are used
    // in the basegame (for fun and education) Ctrl+click on the PlayerClass, CardColor and/or LibraryType below and go down the
    // Ctrl+click rabbit hole

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass WANDERER;
        @SpireEnum(name = "WANDERER_GRAY_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor WANDERER_CARD_COLOR;
        @SpireEnum(name = "WANDERER_GRAY_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType WANDERER_LIBRARY_COLOR;
    }
    
    // Note: These have to live in a separate static subclass to ensure the BaseMode.addColor call can happen before the
    // static initializers for the class run, due to temporal coupling between the abstract base class initializers.
    public static class ColorInfo {
        // Character Color
        public static final Color CHARACTER_COLOR = new Color(0.7f, 0.8f, 1.0f, 1.0f);

        // Card backgrounds - The actual rectangular card.
        public static final String CARD_BG_ATTACK_TEXTURE = makeCharPath("wanderer/card_bgs/card_bg_attack_512.png");
        public static final String CARD_BG_ATTACK_PORTRAIT_TEXTURE = makeCharPath("wanderer/card_bgs/card_bg_attack_1024.png");
        public static final String CARD_BG_SKILL_TEXTURE = makeCharPath("wanderer/card_bgs/card_bg_skill_512.png");
        public static final String CARD_BG_SKILL_PORTRAIT_TEXTURE = makeCharPath("wanderer/card_bgs/card_bg_skill_1024.png");
        public static final String CARD_BG_POWER_TEXTURE = makeCharPath("wanderer/card_bgs/card_bg_power_512.png");
        public static final String CARD_BG_POWER_PORTRAIT_TEXTURE = makeCharPath("wanderer/card_bgs/card_bg_power_1024.png");
        public static final String CARD_OVERLAY_ENERGY_ORB_TEXTURE = makeCharPath("wanderer/card_bgs/card_overlay_energy_orb_512.png");
        public static final String CARD_SMALL_ENERGY_ORB_TEXTURE = makeCharPath("wanderer/card_bgs/card_small_energy_orb.png");
        public static final String CARD_ENERGY_ORB_PORTRAIT_TEXTURE = makeCharPath("wanderer/card_bgs/card_energy_orb.png");

        public static void registerColorWithBaseMod() {
            BaseMod.addColor(
                    Enums.WANDERER_CARD_COLOR,
                    CHARACTER_COLOR,
                    CHARACTER_COLOR,
                    CHARACTER_COLOR,
                    CHARACTER_COLOR,
                    CHARACTER_COLOR,
                    CHARACTER_COLOR,
                    CHARACTER_COLOR,
                    CARD_BG_ATTACK_TEXTURE,
                    CARD_BG_SKILL_TEXTURE,
                    CARD_BG_POWER_TEXTURE,
                    CARD_OVERLAY_ENERGY_ORB_TEXTURE,
                    CARD_BG_ATTACK_PORTRAIT_TEXTURE,
                    CARD_BG_SKILL_PORTRAIT_TEXTURE,
                    CARD_BG_POWER_PORTRAIT_TEXTURE,
                    CARD_ENERGY_ORB_PORTRAIT_TEXTURE,
                    CARD_SMALL_ENERGY_ORB_TEXTURE);
        }
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 60;
    public static final int MAX_HP = 64;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    private static final String ID = makeID("WandererCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES ===============
    
    // Character assets
    public static final String CHARACTER_SELECT_BUTTON_TEXTURE = makeCharPath("wanderer/char_select_button.png");
    public static final String CHARACTER_SELECT_BG_TEXTURE = makeCharPath("wanderer/char_select_bg.png");
    public static final String SHOULDER_DARK_TEXTURE = makeCharPath("wanderer/shoulder_dark.png");
    public static final String SHOULDER_LIT_TEXTURE = makeCharPath("wanderer/shoulder_lit.png");
    public static final String CORPSE_TEXTURE = makeCharPath("wanderer/corpse.png");

    public static final String[] ENERGY_ORB_LAYER_TEXTURES = {
            makeCharPath("wanderer/energy_orb/layer1.png"),
            makeCharPath("wanderer/energy_orb/layer2.png"),
            makeCharPath("wanderer/energy_orb/layer3.png"),
            makeCharPath("wanderer/energy_orb/layer4.png"),
            makeCharPath("wanderer/energy_orb/layer5.png"),
            makeCharPath("wanderer/energy_orb/layer6.png"),
            makeCharPath("wanderer/energy_orb/layer1d.png"),
            makeCharPath("wanderer/energy_orb/layer2d.png"),
            makeCharPath("wanderer/energy_orb/layer3d.png"),
            makeCharPath("wanderer/energy_orb/layer4d.png"),
            makeCharPath("wanderer/energy_orb/layer5d.png"),};

    // =============== /TEXTURES/ ===============


    public Wanderer(String name, PlayerClass setClass) {
        super(
                name,
                setClass,
                ENERGY_ORB_LAYER_TEXTURES,
                makeCharPath("wanderer/energy_orb/vfx.png"),
                null,
                new SpriterAnimation(makeCharPath("wanderer/idle_animation/idleanimation.scml")));

        ((SpriterAnimation)this.animation).myPlayer.setScale(Settings.scale * 1.1F);

        initializeClass(
                null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                SHOULDER_DARK_TEXTURE, // campfire pose
                SHOULDER_LIT_TEXTURE, // another campfire pose
                CORPSE_TEXTURE, // dead corpse
                getLoadout(), 20.0F, -10.0F, 220.0F, 290.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        this.memories = new MemoryManager(this);

        this.dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        this.dialogY = (drawY + 220.0F * Settings.scale); // you can just copy these values
    }

    public final MemoryManager memories;

    @Override
    public void renderHealth(SpriteBatch sb) {
        super.renderHealth(sb);
        memories.render(sb);
    }

    @Override
    public void updatePowers() {
        super.updatePowers();
        memories.update(drawX, drawY);
    }

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        logger.info("Constructing Wanderer starting deck");

        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(Strike_Wanderer.ID);
        retVal.add(Strike_Wanderer.ID);
        retVal.add(Strike_Wanderer.ID);
        retVal.add(Strike_Wanderer.ID);
        retVal.add(Defend_Wanderer.ID);
        retVal.add(Defend_Wanderer.ID);
        retVal.add(Defend_Wanderer.ID);
        retVal.add(Defend_Wanderer.ID);
        retVal.add(FreshAdventure.ID);
        retVal.add(EyeOfTheStorm.ID);
        retVal.add(ForbiddenGrimoire.ID);

        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        // Note: only the first relic gets replaced when selecting the "replace starter relic" Neow boon
        retVal.add(WandererStarterRelic.ID);
        retVal.add(FragileMindRelic.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", 0.8f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return WANDERER_CARD_COLOR;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return ColorInfo.CHARACTER_COLOR;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new FreshAdventure();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new Wanderer(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return ColorInfo.CHARACTER_COLOR;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return ColorInfo.CHARACTER_COLOR;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.POISON,
                AbstractGameAction.AttackEffect.FIRE};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    // When you defeat the heart, this happens
    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList<CutscenePanel>();
        panels.add(new CutscenePanel(makeScenePath("wanderer_heart_kill_1.png"), "ATTACK_DEFECT_BEAM"));
        panels.add(new CutscenePanel(makeScenePath("wanderer_heart_kill_2.png")));
        panels.add(new CutscenePanel(makeScenePath("wanderer_heart_kill_3.png")));
        return panels;
    }

    // Required for beta branch support
    // @Override (uncomment once released)
    public String getPortraitImageName() {
        return CHARACTER_SELECT_BG_TEXTURE;
    }
}
