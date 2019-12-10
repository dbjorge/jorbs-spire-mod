package stsjorbsmod.util;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class PowerUtils {
    public static int getPowerAmount(AbstractCreature creature, String powerID) {
        AbstractPower possiblePower = AbstractDungeon.player.getPower(powerID);
        return (possiblePower == null ? 0 : possiblePower.amount);
    }
}
