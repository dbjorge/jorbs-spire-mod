package stsjorbsmod.twitch;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;

import java.util.ArrayList;

@SpireInitializer
public class TwitchExtensionAPI implements PostUpdateSubscriber {

    // =============== API for SlayTheRelics for displaying tooltips on Twitch =================
    //
    // -------> See <INSERT FUTURE LINK TO THE DOCS> for the documentation of this API <--------
    //
    // These two properties are read by another mod that sends their contents over to a Twitch extension called
    // SlayTheRelics and they are displayed alongside other tooltips.
    public static ArrayList<Hitbox> slayTheRelicsHitboxes = new ArrayList<>();
    public static ArrayList<ArrayList<PowerTip>> slayTheRelicsPowerTips = new ArrayList<>();

    public static TwitchExtensionAPI instance;

    public static void initialize() {
        instance = new TwitchExtensionAPI();
    }

    TwitchExtensionAPI() {
         BaseMod.subscribe(this);
    }

    public static void clearLists() {
        slayTheRelicsHitboxes.clear();
        slayTheRelicsPowerTips.clear();
    }

    public static void addPowerTips(Hitbox hitbox, ArrayList<PowerTip> tips) {
        slayTheRelicsHitboxes.add(hitbox);
        slayTheRelicsPowerTips.add(tips);
    }

    // This ensures that the shared array lists will be empty even after the player exits a run, no matter how,
    // whether they win, lose, save&exit or abandon the run.
    @Override
    public void receivePostUpdate() {
        if (!CardCrawlGame.isInARun()) {
            clearLists();
        }
    }
}
