package stsjorbsmod.memories;

import basemod.BaseMod;
import basemod.devcommands.power.Power;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.powers.CoilPower;

import java.util.ArrayList;

public class PatienceMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(PatienceMemory.class);

    private static final int COIL_PER_CARD = 1;

    public PatienceMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.VIRTUE, owner);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster target) {
        if (isPassiveEffectActive()) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(owner, owner, new CoilPower(owner, COIL_PER_CARD)));
        }
    }

    @Override
    protected void addExtraPowerTips(ArrayList<PowerTip> tips) {
        tips.add(new PowerTip(
                BaseMod.getKeywordTitle("stsjorbsmod:coil"),
                BaseMod.getKeywordDescription("stsjorbsmod:coil"),
                CoilPower.STATIC.IMG_32));
    }
}
