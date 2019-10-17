package stsjorbsmod;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.abstracts.CustomCard;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.cards.*;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.console.MemoryCommand;
import stsjorbsmod.events.DeckOfManyThingsEvent;
import stsjorbsmod.relics.*;
import stsjorbsmod.util.TextureLoader;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class JorbsMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {
    public static final String MOD_ID = "stsjorbsmod";

    public static final Logger logger = LogManager.getLogger(JorbsMod.class.getName());

    // Mod-settings settings. This is if you want an on/off savable button
    public static Properties theDefaultDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true; // The boolean we'll be setting on/off (true/false)

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Jorbs Mod";
    private static final String AUTHOR = "Twitch chat"; // And pretty soon - You!
    private static final String DESCRIPTION = "New characters, brought to you by Jorbs and Twitch chat!";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    // Colors (RGB)
    // Character Color
    public static final Color DEFAULT_GRAY = CardHelper.getColor(64.0f, 70.0f, 70.0f);

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = "stsjorbsmodResources/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "stsjorbsmodResources/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "stsjorbsmodResources/images/512/bg_power_default_gray.png";
    
    private static final String ENERGY_ORB_DEFAULT_GRAY = "stsjorbsmodResources/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "stsjorbsmodResources/images/512/card_small_orb.png";
    
    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "stsjorbsmodResources/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "stsjorbsmodResources/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "stsjorbsmodResources/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "stsjorbsmodResources/images/1024/card_default_gray_orb.png";
    
    // Character assets
    private static final String THE_DEFAULT_BUTTON = "stsjorbsmodResources/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "stsjorbsmodResources/images/charSelect/DefaultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "stsjorbsmodResources/images/char/wanderer/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "stsjorbsmodResources/images/char/wanderer/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "stsjorbsmodResources/images/char/wanderer/corpse.png";
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "stsjorbsmodResources/images/Badge.png";
    
    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "stsjorbsmodResources/images/char/wanderer/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "stsjorbsmodResources/images/char/wanderer/skeleton.json";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return MOD_ID + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return MOD_ID + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return MOD_ID + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makeOrbPath(String resourcePath) {
        return MOD_ID + "Resources/orbs/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return MOD_ID + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return MOD_ID + "Resources/images/events/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public JorbsMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);

        logger.info("Done subscribing");
        
        logger.info("Creating the color " + Wanderer.Enums.COLOR_GRAY.toString());
        
        BaseMod.addColor(Wanderer.Enums.COLOR_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY, DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);
        
        logger.info("Done creating the color");
        
        
        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        theDefaultDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");
        
    }
    
    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing JorbsMod. !dig =========================");
        JorbsMod defaultmod = new JorbsMod();
        logger.info("========================= /JorbsMod Initialized./ =========================");
    }
    
    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================
    
    
    // =============== LOAD THE CHARACTER =================
    
    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + Wanderer.Enums.WANDERER.toString());
        
        BaseMod.addCharacter(new Wanderer("The Wanderer", Wanderer.Enums.WANDERER),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, Wanderer.Enums.WANDERER);

        logger.info("Added " + Wanderer.Enums.WANDERER.toString());
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        MemoryCommand.register();

        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        // Create the on/off button:
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:
            
            enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
            try {
                // And based on that boolean, set the settings and save them
                SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", theDefaultDefaultSettings);
                config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                config.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        
        // =============== EVENTS =================
        
        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        BaseMod.addEvent(DeckOfManyThingsEvent.ID, DeckOfManyThingsEvent.class, TheCity.ID);
        
        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================

    
    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        
        // Character-specific relics for custom characters use BaseMod.addRelicToCustomPool
        BaseMod.addRelicToCustomPool(new WandererStarterRelic(), Wanderer.Enums.COLOR_GRAY);
        UnlockTracker.markRelicAsSeen(WandererStarterRelic.ID);
        BaseMod.addRelicToCustomPool(new FragileMindRelic(), Wanderer.Enums.COLOR_GRAY);
        UnlockTracker.markRelicAsSeen(FragileMindRelic.ID);

        // Shared (non-character-specific) relics would instead use this:
        // BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================

    private void addUnlockedCard(CustomCard cardInstance) {
        BaseMod.addCard(cardInstance);
        UnlockTracker.unlockCard(cardInstance.cardID);
    }

    @Override
    public void receiveEditCards() {
        logger.info("Adding cards");

        // === WATCHER ===
        // == Starter Deck
        addUnlockedCard(new Defend_Wanderer());
        addUnlockedCard(new Strike_Wanderer());
        addUnlockedCard(new FreshAdventure());
        addUnlockedCard(new EyeOfTheStorm());
        // == Damage Commons
        addUnlockedCard(new BlackTentacles());
        addUnlockedCard(new ArcaneWeapon());
        addUnlockedCard(new Firebolt());
        addUnlockedCard(new MagicMissiles());
        addUnlockedCard(new AcidSplash());
        addUnlockedCard(new TrueStrike());
        addUnlockedCard(new RayOfFrost());
        // == Block Commons
        addUnlockedCard(new Counterspell());
        addUnlockedCard(new MinorIllusion());
        addUnlockedCard(new DisguiseSelf());
        addUnlockedCard(new Loss());
        addUnlockedCard(new DoubleCheck());
        addUnlockedCard(new Channel());
        // == AoE Commons
        addUnlockedCard(new PoisonSpray());
        addUnlockedCard(new ChainLightning());
        // == Scaling Commons
        addUnlockedCard(new WanderingMind());
        addUnlockedCard(new WeightOfMemory());
        addUnlockedCard(new PrestidigitationA());
        // addUnlockedCard(new WizardHat());
        addUnlockedCard(new WizardRobe());
        // == Manipulation Commons
        addUnlockedCard(new Message());
        addUnlockedCard(new PrestidigitationB());
        addUnlockedCard(new FocusedMind());
        addUnlockedCard(new UnseenServant());
        // == Bad Uncommons
        addUnlockedCard(new Aid());
        addUnlockedCard(new Mania());
        // == Damage Uncommons
        addUnlockedCard(new Mindworm());
        addUnlockedCard(new Hurt());
        addUnlockedCard(new SmithingStrike());
        addUnlockedCard(new OldPocket());
        addUnlockedCard(new TollTheDead());
        // addUnlockedCard(new CorpseExplosion_Wanderer());
        // == Block Uncommons
        addUnlockedCard(new Hibernate());
        addUnlockedCard(new Mending());
        addUnlockedCard(new Rest());
        addUnlockedCard(new HedgeWizard());
        addUnlockedCard(new MistyStep());
        // addUnlockedCard(new MageArmor());
        // addUnlockedCard(new Enervation());
        // == AoE Uncommons
        // addUnlockedCard(new Stalwart());
        // addUnlockedCard(new AnimateObjects());
        // addUnlockedCard(new ColorSpray());
        // addUnlockedCard(new Erode());
        // == Scaling Uncommons
        // addUnlockedCard(new TinyHut());
        // addUnlockedCard(new FaerieFire());
        // addUnlockedCard(new Introspection());
        // addUnlockedCard(new BookOfTongues());
        // addUnlockedCard(new MagicMirror());
        // addUnlockedCard(new Thorns());
        // ==Manipulation Uncommons
        // addUnlockedCard(new RefuseToForget());
        // addUnlockedCard(new LocateObject());
        // addUnlockedCard(new SchoolsOfMagic());
        addUnlockedCard(new FindFamiliar());
        // == Damage Rares
        // addUnlockedCard(new Trauma());
        // addUnlockedCard(new GatherPower());
        // addUnlockedCard(new Haste());
        addUnlockedCard(new Fireball());
        // ==Block Rares
        // addUnlockedCard(new Lose());
        // addUnlockedCard(new HoldMonster());
        addUnlockedCard(new Withdraw());
        // addUnlockedCard(new MirrorImage());
        // addUnlockedCard(new CorrodingBarrier());
        // ==AoE Rares
        // addUnlockedCard(new OldFriends());
        // addUnlockedCard(new Entangle());
        // ==Bad Rares
        // addUnlockedCard(new Amnesia());
        // addUnlockedCard(new Fear());
        // ==Scaling Rares
        // addUnlockedCard(new Wish_Wanderer());
        // addUnlockedCard(new Determination());
        // addUnlockedCard(new ArcaneForm());
        // addUnlockedCard(new FocusOnThePain());
        addUnlockedCard(new Study());
        // addUnlockedCard(new Banish());
        // ==Manipulation Rares
        // addUnlockedCard(new TimeWalk());
        // addUnlockedCard(new Feast());
        // addUnlockedCard(new SharpenedMind());
        // addUnlockedCard(new Ivory());
        // addUnlockedCard(new DimensionDoor());

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + MOD_ID);

        // UIStrings
        BaseMod.loadCustomStringsFile(UIStrings.class,
                MOD_ID + "Resources/localization/eng/JorbsMod-UI-Strings.json");

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                MOD_ID + "Resources/localization/eng/JorbsMod-Card-Strings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                MOD_ID + "Resources/localization/eng/JorbsMod-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                MOD_ID + "Resources/localization/eng/JorbsMod-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                MOD_ID + "Resources/localization/eng/JorbsMod-Event-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                MOD_ID + "Resources/localization/eng/JorbsMod-Character-Strings.json");

        logger.info("Done editing strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(MOD_ID + "Resources/localization/eng/JorbsMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(MOD_ID.toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return MOD_ID + ":" + idText;
    }
}
