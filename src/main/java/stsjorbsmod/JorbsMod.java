package stsjorbsmod;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.cards.CardSaveData;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.console.MemoryCommand;
import stsjorbsmod.potions.BurningPotion;
import stsjorbsmod.potions.DimensionDoorPotion;
import stsjorbsmod.relics.FragileMindRelic;
import stsjorbsmod.relics.MindGlassRelic;
import stsjorbsmod.relics.WandererStarterRelic;
import stsjorbsmod.util.ReflectionUtils;
import stsjorbsmod.util.TextureLoader;
import stsjorbsmod.variables.BaseBlockNumber;
import stsjorbsmod.variables.BaseDamageNumber;
import stsjorbsmod.variables.MetaMagicNumber;
import stsjorbsmod.variables.UrMagicNumber;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "stsjorbsmodResources/images/Badge.png";

    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return MOD_ID + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return MOD_ID + "Resources/images/relics/" + resourcePath;
    }

    public static String makeCharPath(String resourcePath) {
        return MOD_ID + "Resources/images/characters/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return MOD_ID + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makeMemoryPath(String resourcePath) {
        return MOD_ID + "Resources/images/memories/" + resourcePath;
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

    public static String makeScenePath(String resourcePath) {
        return MOD_ID + "Resources/images/scenes/" + resourcePath;
    }

    public static String makeLocalizedStringsPath(String resourcePath) {
        String languageFolder =
                // Settings.language == Settings.GameLanguage.FRA ? "fra" :
                /* default: */ "eng";

        return MOD_ID + "Resources/localization/" + languageFolder + "/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public JorbsMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);

        logger.info("Done subscribing");
        
        logger.info("Creating new card colors..." + Wanderer.Enums.WANDERER_CARD_COLOR.toString());

        Wanderer.ColorInfo.registerColorWithBaseMod();
        
        logger.info("Done creating colors");
        
        
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

        logger.info("Adding save fields");
        BaseMod.addSaveField(MOD_ID + ":CardSaveData", new CardSaveData());
        logger.info("Done adding save fields");
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
        
        BaseMod.addCharacter(
                new Wanderer("The Wanderer", Wanderer.Enums.WANDERER),
                Wanderer.CHARACTER_SELECT_BUTTON_TEXTURE,
                Wanderer.CHARACTER_SELECT_BG_TEXTURE,
                Wanderer.Enums.WANDERER);

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

        BaseMod.addPotion(DimensionDoorPotion.class, Color.BLACK, Color.CORAL, null,
                DimensionDoorPotion.POTION_ID, Wanderer.Enums.WANDERER);
        BaseMod.addPotion(BurningPotion.class, Color.ORANGE, null, new Color(-1033371393),
                BurningPotion.POTION_ID, Wanderer.Enums.WANDERER);
        
        // =============== EVENTS =================
        
        // This event will be exclusive to the City (act 2). If you want an event that's present at any
        // part of the game, simply don't include the dungeon ID
        // If you want to have a character-specific event, look at slimebound (CityRemoveEventPatch).
        // Essentially, you need to patch the game and say "if a player is not playing my character class, remove the event from the pool"
        //
        // Not complete yet:
        // BaseMod.addEvent(DeckOfManyThingsEvent.ID, DeckOfManyThingsEvent.class, TheCity.ID);
        
        // =============== /EVENTS/ =================
        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================

    
    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");
        
        // Character-specific relics for custom characters use BaseMod.addRelicToCustomPool
        BaseMod.addRelicToCustomPool(new WandererStarterRelic(), Wanderer.Enums.WANDERER_CARD_COLOR);
        UnlockTracker.markRelicAsSeen(WandererStarterRelic.ID);
        BaseMod.addRelicToCustomPool(new FragileMindRelic(), Wanderer.Enums.WANDERER_CARD_COLOR);
        UnlockTracker.markRelicAsSeen(FragileMindRelic.ID);
        BaseMod.addRelicToCustomPool(new MindGlassRelic(), Wanderer.Enums.WANDERER_CARD_COLOR);
        UnlockTracker.markBossAsSeen(MindGlassRelic.ID);

        // Shared (non-character-specific) relics would instead use this:
        // BaseMod.addRelic(new PlaceholderRelic2(), RelicType.SHARED);

        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding cards");

        BaseMod.addDynamicVariable(new BaseBlockNumber());
        BaseMod.addDynamicVariable(new BaseDamageNumber());
        BaseMod.addDynamicVariable(new UrMagicNumber());
        BaseMod.addDynamicVariable(new MetaMagicNumber());

        ArrayList<Class<CustomJorbsModCard>> cardClasses = ReflectionUtils.findAllConcreteSubclasses(CustomJorbsModCard.class);
        for (Class<CustomJorbsModCard> cardClass : cardClasses) {
            try {
                CustomJorbsModCard cardInstance = cardClass.newInstance();
                logger.info("Adding card: " + cardInstance.cardID);
                BaseMod.addCard(cardInstance);
                UnlockTracker.unlockCard(cardInstance.cardID);
            } catch(Exception e) {
                throw new RuntimeException("Exception while instantiating CustomJorbsModCard " + cardClass.getName(), e);
            }
        }

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + MOD_ID);

        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocalizedStringsPath("JorbsMod-UI-Strings.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizedStringsPath("JorbsMod-Card-Strings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizedStringsPath("JorbsMod-Power-Strings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocalizedStringsPath("JorbsMod-Relic-Strings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocalizedStringsPath("JorbsMod-Event-Strings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocalizedStringsPath("JorbsMod-Character-Strings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocalizedStringsPath("JorbsMod-Potion-Strings.json"));

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
        String json = Gdx.files.internal( makeLocalizedStringsPath("JorbsMod-Keyword-Strings.json")).readString(String.valueOf(StandardCharsets.UTF_8));
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
