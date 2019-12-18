package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.powers.CoilPower;

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
}
