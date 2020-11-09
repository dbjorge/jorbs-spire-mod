package stsjorbsmod.characters;

import basemod.BaseMod;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.DeathScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.actions.DecreaseMaxHpAction;
import stsjorbsmod.actions.GainSpecificClarityAction;
import stsjorbsmod.actions.IncreaseManifestAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.audio.VoiceoverMaster;
import stsjorbsmod.cards.cull.*;
import stsjorbsmod.memories.WrathMemory;
import stsjorbsmod.relics.BookOfTrialsRelic;

import java.util.ArrayList;
import java.util.List;

import static stsjorbsmod.JorbsMod.makeCharPath;
import static stsjorbsmod.JorbsMod.makeID;

//Wiki-page https://github.com/daviscook477/BaseMod/wiki/Custom-Characters
//and https://github.com/daviscook477/BaseMod/wiki/Migrating-to-5.0
//All text (starting description and loadout, anything labeled TEXT[]) can be found in JorbsMod-Character-Strings.json in the resources

public class Cull extends CustomPlayer implements OnAfterPlayerHpLossSubscriber {
    public static final Logger logger = LogManager.getLogger(Cull.class.getName());

    // =============== CHARACTER ENUMERATORS =================
    // These are enums for your Characters color (both general color and for the card library) as well as
    // an enum for the name of the player class - IRONCLAD, THE_SILENT, DEFECT, YOUR_CLASS ...
    // These are all necessary for creating a character. If you want to find out where and how exactly they are used
    // in the basegame (for fun and education) Ctrl+click on the PlayerClass, CardColor and/or LibraryType below and go down the
    // Ctrl+click rabbit hole

    public static class Enums {
        @SpireEnum
        public static PlayerClass CULL;
        @SpireEnum(name = "CULL_RED_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor CULL_CARD_COLOR;
        @SpireEnum(name = "CULL_RED_COLOR") @SuppressWarnings("unused")
        public static CardLibrary.LibraryType CULL_LIBRARY_COLOR;
    }

    // Note: These have to live in a separate static subclass to ensure the BaseMode.addColor call can happen before the
    // static initializers for the class run, due to temporal coupling between the abstract base class initializers.
    public static class ColorInfo {
        // Character Color
        public static final Color CHARACTER_COLOR = new Color(0.9f, 0.6f, 0.6f, 1.0f);

        // Card backgrounds - The actual rectangular card.
        public static final String CARD_BG_ATTACK_TEXTURE = makeCharPath("cull/card_bgs/card_bg_attack_512.png");
        public static final String CARD_BG_ATTACK_PORTRAIT_TEXTURE = makeCharPath("cull/card_bgs/card_bg_attack_1024.png");
        public static final String CARD_BG_SKILL_TEXTURE = makeCharPath("cull/card_bgs/card_bg_skill_512.png");
        public static final String CARD_BG_SKILL_PORTRAIT_TEXTURE = makeCharPath("cull/card_bgs/card_bg_skill_1024.png");
        public static final String CARD_BG_POWER_TEXTURE = makeCharPath("cull/card_bgs/card_bg_power_512.png");
        public static final String CARD_BG_POWER_PORTRAIT_TEXTURE = makeCharPath("cull/card_bgs/card_bg_power_1024.png");
        public static final String CARD_OVERLAY_ENERGY_ORB_TEXTURE = makeCharPath("cull/card_bgs/card_overlay_energy_orb_512.png");
        public static final String CARD_SMALL_ENERGY_ORB_TEXTURE = makeCharPath("cull/card_bgs/card_small_energy_orb.png");
        public static final String CARD_ENERGY_ORB_PORTRAIT_TEXTURE = makeCharPath("cull/card_bgs/card_energy_orb.png");

        public static void registerColorWithBaseMod() {
            BaseMod.addColor(
                    Enums.CULL_CARD_COLOR,
                    CHARACTER_COLOR.cpy(),
                    CHARACTER_COLOR.cpy(),
                    CHARACTER_COLOR.cpy(),
                    CHARACTER_COLOR.cpy(),
                    CHARACTER_COLOR.cpy(),
                    CHARACTER_COLOR.cpy(),
                    CHARACTER_COLOR.cpy(),
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

    public static class AudioInfo {
        private static void registerVoiceOver(String key) {
            registerVoiceOver(key, key);
        }

        private static void registerVoiceOver(String key, String baseName) {
            baseName = baseName.replace(' ', '_');
            String fileName = "cull/" + baseName + ".ogg";
            String subtitle = CardCrawlGame.languagePack.getUIString(makeID("cull:voiceover:"+baseName)).TEXT[0];
            VoiceoverMaster.register(Cull.Enums.CULL, key, fileName, subtitle);
        }

        public static void registerAudio() {
            // See MonsterHelper.getEncounter for IDs
            registerVoiceOver("3 Sentries");
            registerVoiceOver("Automaton");
            registerVoiceOver("Awakened One");
            registerVoiceOver("Book of Stabbing");
            registerVoiceOver("Champ");
            registerVoiceOver("Collector");
            registerVoiceOver("Donu and Deca");
            registerVoiceOver("Giant Head");
            registerVoiceOver("Gremlin Leader");
            registerVoiceOver("Gremlin Nob");
            registerVoiceOver("Hexaghost");
            registerVoiceOver("Lagavulin");
            registerVoiceOver("Nemesis");
            registerVoiceOver("Reptomancer");
            registerVoiceOver("Shield and Spear");
            registerVoiceOver("Slavers");
            registerVoiceOver("Slime Boss");
            registerVoiceOver("The Guardian");
            registerVoiceOver("The Heart");
            registerVoiceOver("Time Eater");
            registerVoiceOver("death");
            registerVoiceOver("death_victory");
            registerVoiceOver("new_run");
        }
    }

    // =============== CHARACTER ENUMERATORS  =================


    // =============== BASE STATS =================

    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 70;
    public static final int MAX_HP = 70;
    public static final int STARTING_GOLD = 199;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    public static final int MANIFEST_PER_TURN = 1;
    public static final int MAX_HP_LOSS_PER_TURN = 1;
    public static final int MAX_HP_LOSS_ON_DAMAGED = 1;

    // =============== /BASE STATS/ =================


    // =============== STRINGS =================

    public static final String ID = makeID("CullCharacter");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    // =============== /STRINGS/ =================


    // =============== TEXTURES ===============

    // Character assets
    public static final String CHARACTER_SELECT_BUTTON_TEXTURE = makeCharPath("cull/char_select_button.png");
    public static final String CHARACTER_SELECT_BG_TEXTURE = makeCharPath("cull/char_select_bg.png");
    public static final String SHOULDER_DARK_TEXTURE = makeCharPath("cull/shoulder_dark.png");
    public static final String SHOULDER_LIT_TEXTURE = makeCharPath("cull/shoulder_lit.png");
    public static final String CORPSE_TEXTURE = makeCharPath("cull/corpse.png");

    public static final String[] ENERGY_ORB_LAYER_TEXTURES = {
            makeCharPath("cull/energy_orb/layer1.png"),
            makeCharPath("cull/energy_orb/layer2.png"),
            makeCharPath("cull/energy_orb/layer3.png"),
            makeCharPath("cull/energy_orb/layer4.png"),
            makeCharPath("cull/energy_orb/layer5.png"),
            makeCharPath("cull/energy_orb/layer6.png"),
            makeCharPath("cull/energy_orb/layer1d.png"),
            makeCharPath("cull/energy_orb/layer2d.png"),
            makeCharPath("cull/energy_orb/layer3d.png"),
            makeCharPath("cull/energy_orb/layer4d.png"),
            makeCharPath("cull/energy_orb/layer5d.png"),};

    private static final float DIALOG_OFFSET_X = 0.0F * Settings.scale;
    private static final float DIALOG_OFFSET_Y = 180.0F * Settings.scale;

    // =============== /TEXTURES/ ===============

    private static SpriterAnimation loadIdleAnimation() {
        SpriterAnimation animation = new SpriterAnimation(makeCharPath("cull/idle_animation/idleanimation.scml"));
        animation.myPlayer.setScale(Settings.scale * 1.1F);
        return animation;
    }

    public Cull(String name, PlayerClass setClass) {
        super(
                name,
                setClass,
                ENERGY_ORB_LAYER_TEXTURES,
                makeCharPath("cull/energy_orb/vfx.png"),
                null,
                loadIdleAnimation());

        initializeClass(
                null,
                SHOULDER_DARK_TEXTURE,
                SHOULDER_LIT_TEXTURE,
                CORPSE_TEXTURE,
                getLoadout(), 0F, -10.0F, 160.0F, 280.0F, new EnergyManager(ENERGY_PER_TURN));

        this.dialogX = drawX + DIALOG_OFFSET_X;
        this.dialogY = drawY + DIALOG_OFFSET_Y;
    }

    @Override
    public void movePosition(float x, float y) {
        super.movePosition(x, y);
        this.dialogX = x + DIALOG_OFFSET_X;
        this.dialogY = y + DIALOG_OFFSET_Y;
    }

    // Starting description and loadout
    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(
                NAMES[0],
                TEXT[0],
                STARTING_HP,
                MAX_HP,
                ORB_SLOTS,
                STARTING_GOLD,
                CARD_DRAW,
                this,
                getStartingRelics(),
                getStartingDeck(),
                false);
    }

    // Starting Deck
    @Override
    public ArrayList<String> getStartingDeck() {
        logger.info("Constructing Cull starting deck");

        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(CULLCard.ID);
        retVal.add(Apparition_Cull.ID);
        retVal.add(Strike_Cull.ID);
        retVal.add(Strike_Cull.ID);
        retVal.add(Strike_Cull.ID);
        retVal.add(Strike_Cull.ID);
        retVal.add(Paralysis.ID);
        retVal.add(SpiritShield_Cull.ID);
        retVal.add(Exhale.ID);
        retVal.add(Withering.ID);

        return retVal;
    }

    // Starting Relics
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(BookOfTrialsRelic.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("BLOOD_SPLAT", 0.8f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "BLOOD_SPLAT";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 10;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.CULL_CARD_COLOR;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return ColorInfo.CHARACTER_COLOR.cpy();
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
        return new Apparition_Cull();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new Cull(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return ColorInfo.CHARACTER_COLOR.cpy();
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return ColorInfo.CHARACTER_COLOR.cpy();
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
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel(makeCharPath("cull/victory_scene/panel_1.png")));
        panels.add(new CutscenePanel(makeCharPath("cull/victory_scene/panel_2.png"), "SHOVEL"));
        panels.add(new CutscenePanel(makeCharPath("cull/victory_scene/panel_3.png")));
        return panels;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AttackEffect[] getSpireHeartSlashEffect() {
        return new AttackEffect[]{
                AttackEffect.BLUNT_HEAVY,
                AttackEffect.FIRE,
                AttackEffect.FIRE};
    }

    @Override
    public String getPortraitImageName() {
        return CHARACTER_SELECT_BG_TEXTURE;
    }

    @Override
    public void applyEndOfTurnTriggers() {
        super.applyEndOfTurnTriggers();

        AbstractDungeon.actionManager.addToBottom(
                new IncreaseManifestAction(MANIFEST_PER_TURN));
        AbstractDungeon.actionManager.addToBottom(
                new DecreaseMaxHpAction(this, this, MAX_HP_LOSS_PER_TURN, AttackEffect.POISON));
    }

    @Override
    public void onAfterPlayerHpLoss(int damageAmount) {
        AbstractDungeon.actionManager.addToBottom(
                new DecreaseMaxHpAction(this, this, MAX_HP_LOSS_ON_DAMAGED, AttackEffect.POISON));
    }

    @Override
    public void applyStartOfCombatLogic() {
        super.applyStartOfCombatLogic();
        AbstractDungeon.actionManager.addToBottom(new RememberSpecificMemoryAction(this, WrathMemory.STATIC.ID));
        AbstractDungeon.actionManager.addToBottom(new GainSpecificClarityAction(this, WrathMemory.STATIC.ID));
    }

    @Override
    public void decreaseMaxHealth(int amount) {
        boolean isFatal = amount >= maxHealth;
        super.decreaseMaxHealth(amount);
        if (isFatal) {
            currentHealth = 0;
            maxHealth = 0;
            isDead = true;
            AbstractDungeon.deathScreen = new DeathScreen(AbstractDungeon.getMonsters());
        }
    }
}
