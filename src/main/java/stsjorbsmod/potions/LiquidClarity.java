package stsjorbsmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.memories.MemoryManager;

public class LiquidClarity extends AbstractPotion {
    public static final String POTION_ID = JorbsMod.makeID(LiquidClarity.class);
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public LiquidClarity() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.BOTTLE, PotionColor.BLUE);
        this.description = DESCRIPTIONS[0];
        this.potency = getPotency();
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle("stsjorbsmod:clarity"), BaseMod.getKeywordDescription("stsjorbsmod:clarity")));
    }

    @Override
    public void use(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new GainClarityOfCurrentMemoryAction(AbstractDungeon.player));
    }

    @Override
    public boolean canUse() {
        MemoryManager mm = MemoryManager.forPlayer(AbstractDungeon.player);
        return super.canUse() && mm != null && !mm.isSnapped() && mm.currentMemory != null && !mm.hasClarity(mm.currentMemory.ID);
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new LiquidClarity();
    }
}
