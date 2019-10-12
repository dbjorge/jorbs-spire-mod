package net.dbjorge.stsjorbsmod;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import net.dbjorge.stsjorbsmod.characters.WandererCharacter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpireInitializer
public class JorbsMod {
    public static final Logger logger = LogManager.getLogger(JorbsMod.class);

    public JorbsMod() {
        //BaseMod.subscribe(this);
        WandererCharacter.registerColor();
    }


    public static void initialize() {
        logger.info("Initializing JorbsMod");
        new JorbsMod();
    }
}