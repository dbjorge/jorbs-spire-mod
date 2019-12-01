package stsjorbsmod.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.actions.GainRandomNewClarityAction;
import stsjorbsmod.actions.RememberRandomNewMemoryAction;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.MemoryType;
import stsjorbsmod.memories.MemoryUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LiquidVirtue extends AbstractPotion {
    public static final String POTION_ID = JorbsMod.makeID(LiquidVirtue.class);
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    private static final int NUM_VIRTUES = 2;

    public LiquidVirtue() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.HEART, PotionColor.BLUE);
        this.description = DESCRIPTIONS[0];
        this.potency = getPotency();
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        for (int i = 0; i < potency; ++i) {
            AbstractDungeon.actionManager.addToBottom(new GainRandomNewClarityAction(AbstractDungeon.player, m -> m.memoryType == MemoryType.VIRTUE));
        }
    }

    private boolean anyCandidateClarities(MemoryManager mm) {
        return MemoryUtils
                .allPossibleMemories(null)
                .stream()
                .anyMatch(m -> m.memoryType == MemoryType.VIRTUE && !mm.hasClarity(m.ID));
    }

    @Override
    public boolean canUse() {
        MemoryManager mm = MemoryManager.forPlayer(AbstractDungeon.player);
        return super.canUse() && mm != null && !mm.isSnapped() && anyCandidateClarities(mm);
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return NUM_VIRTUES;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new LiquidVirtue();
    }
}
