package stsjorbsmod;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clapper.util.classutil.RegexClassFilter;
import stsjorbsmod.cards.CardSaveData;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.characters.ManifestSaveData;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.console.MemoryCommand;
import stsjorbsmod.console.PlaySoundCommand;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.potions.BurningPotion;
import stsjorbsmod.potions.DimensionDoorPotion;
import stsjorbsmod.potions.LiquidClarity;
import stsjorbsmod.potions.LiquidVirtue;
import stsjorbsmod.relics.CustomJorbsModRelic;
import stsjorbsmod.util.ReflectionUtils;
import stsjorbsmod.util.TextureLoader;
import stsjorbsmod.variables.BaseBlockNumber;
import stsjorbsmod.variables.BaseDamageNumber;
import stsjorbsmod.variables.MetaMagicNumber;
import stsjorbsmod.variables.UrMagicNumber;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SpireInitializer
public class JorbsMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        OnPowersModifiedSubscriber {
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

    public static class JorbsCardTags {
        // Use on a card that brings in a possible beneficial effect that lasts longer than the combat and isn't
        // directly healing or gaining Max HP. If the effect has indirect healing, such as adding a second effect
        // that conditionally heals or grants Max HP, do use this card tag instead of HEALING.
        @SpireEnum(name = "PERSISTENT_POSITIVE_EFFECT")
        public static AbstractCard.CardTags PERSISTENT_POSITIVE_EFFECT;

        // Legendary cards (cards with the LEGENDARY tag) have the following special qualities:
        // - Cannot be duplicated, removed, or transformed
        // - Can only be obtained once per run (are removed from pools after being obtained the first time)
        // - (not implemented yet) Explorer Legendaries can only be found after the Act 2 boss
        //
        // Interaction notes:
        // - Any red, green, or blue cards that duplicate a card could pick a Legendary card. These behaviors remain
        //   because this mod is not designed to interact with colored cards from the main game.
        //   Known cards: Dual Wield, Nightmare
        // - Living Wall event: if the player has no purgeable cards, they won't be given a chance to upgrade.
        //     Spire bug #21944: Living Wall event checks for purgeable cards
        //     in the Grow choice instead of upgradeable cards.
        // - Designer event: the Remove option can be active without any purgeable cards to select from.
        //     Spire bug #21945: Designer event checks size of master deck for
        //     enabling Remove/Transform option instead of purgeable cards.
        // - All Star mod won't give any Legendary colorless cards (should they ever exist).
        // - Hoarder mod will add two additional copies of Legendary cards.
        // - Insanity mod has access to Legendary cards and can generate duplicates.
        // - Specialized mod can start with five of the same Legendary card, if Draft or Sealed is enabled.
        @SpireEnum(name = "LEGENDARY")
        public static AbstractCard.CardTags LEGENDARY;

        // Use on a card that remembers a memory, which is mechanic specific to the Wanderer character.
        @SpireEnum(name = "REMEMBER_MEMORY")
        public static AbstractCard.CardTags REMEMBER_MEMORY;
    }

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

    public static String makeMonsterPath(String resourcePath) {
        return MOD_ID + "Resources/images/monsters/" + resourcePath;
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
                Settings.language == Settings.GameLanguage.FRA ? "fra" :
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
        
        logger.info("Creating new card colors...");

        Cull.ColorInfo.registerColorWithBaseMod();
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
        BaseMod.addSaveField(MOD_ID + ":ManifestSaveData", new ManifestSaveData());
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
        logger.info("Beginning to edit characters.");

        // order matters; this will be the order they appear in the char select screen

        logger.info("Adding Wanderer...");
        BaseMod.addCharacter(
                new Wanderer("The Wanderer", Wanderer.Enums.WANDERER),
                Wanderer.CHARACTER_SELECT_BUTTON_TEXTURE,
                Wanderer.CHARACTER_SELECT_BG_TEXTURE,
                Wanderer.Enums.WANDERER);

        logger.info("Adding Cull...");
        BaseMod.addCharacter(
                new Cull("The Cull", Cull.Enums.CULL),
                Cull.CHARACTER_SELECT_BUTTON_TEXTURE,
                Cull.CHARACTER_SELECT_BG_TEXTURE,
                Cull.Enums.CULL);

        logger.info("Added characters");
    }
    
    // =============== /LOAD THE CHARACTER/ =================
    
    
    // =============== POST-INITIALIZE =================

    private static void registerPowerInDevConsole(Class<? extends AbstractPower> jorbsModPower) {
        try {
            String id = (String)jorbsModPower.getField("POWER_ID").get(null);
            logger.info("Registering power: " + id);
            BaseMod.addPower(jorbsModPower, id);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerPowersInDevConsole() {
        logger.info("Registering powers in developer console");

        ArrayList<Class<AbstractPower>> powers = ReflectionUtils.findAllConcreteJorbsModClasses(new RegexClassFilter("^stsjorbsmod\\.powers\\.(.+)Power$"));
        for(Class<AbstractPower> power : powers) {
            registerPowerInDevConsole(power);
        }

        logger.info("Done registering powers");
    }

    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");

        MemoryCommand.register();
        PlaySoundCommand.register();

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
        BaseMod.addPotion(LiquidClarity.class, Color.BLUE, null, Color.PURPLE,
                LiquidClarity.POTION_ID, Wanderer.Enums.WANDERER);
        BaseMod.addPotion(LiquidVirtue.class, Color.BLUE, null, Color.PURPLE,
                LiquidVirtue.POTION_ID, Wanderer.Enums.WANDERER);
        
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

        registerPowersInDevConsole();
    }
    
    // =============== / POST-INITIALIZE/ =================

    
    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        List<CustomJorbsModRelic> relics = ReflectionUtils.instantiateAllConcreteJorbsModSubclasses(CustomJorbsModRelic.class);
        for (CustomJorbsModRelic relicInstance : relics) {
            logger.info("Adding relic: " + relicInstance.relicId);
            if (relicInstance.relicColor.equals(AbstractCard.CardColor.COLORLESS)) {
                BaseMod.addRelic(relicInstance, RelicType.SHARED);
            } else {
                BaseMod.addRelicToCustomPool(relicInstance, relicInstance.relicColor);
            }
            UnlockTracker.markRelicAsSeen(relicInstance.relicId);
        }

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

        List<CustomJorbsModCard> cards = ReflectionUtils.instantiateAllConcreteJorbsModSubclasses(CustomJorbsModCard.class);
        for (CustomJorbsModCard cardInstance : cards) {
            logger.info("Adding card: " + cardInstance.cardID);
            BaseMod.addCard(cardInstance);
            UnlockTracker.unlockCard(cardInstance.cardID);
        }

        logger.info("Done adding cards!");
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings for mod with ID: " + MOD_ID);

        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizedStringsPath("JorbsMod-Card-Strings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocalizedStringsPath("JorbsMod-Character-Strings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class, makeLocalizedStringsPath("JorbsMod-Event-Strings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizedStringsPath("JorbsMod-Memory-Strings.json"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class, makeLocalizedStringsPath("JorbsMod-Monster-Strings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class, makeLocalizedStringsPath("JorbsMod-Potion-Strings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizedStringsPath("JorbsMod-Power-Strings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, makeLocalizedStringsPath("JorbsMod-Relic-Strings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, makeLocalizedStringsPath("JorbsMod-UI-Strings.json"));

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
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return MOD_ID + ":" + idText;
    }

    public static String makeID(Class idClass) {
        return makeID(idClass.getSimpleName());
    }

    @Override
    public void receivePowersModified() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT &&
                !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof OnPowersModifiedSubscriber) {
                    ((OnPowersModifiedSubscriber)p).receivePowersModified();
                }
            }
            MemoryManager mm = MemoryManager.forPlayer();
            if (mm != null) {
                for (AbstractMemory m : mm.allMemoriesIncludingInactive()) {
                    if (m instanceof OnPowersModifiedSubscriber) {
                        ((OnPowersModifiedSubscriber)m).receivePowersModified();
                    }
                }
            }
        }
    }
}
