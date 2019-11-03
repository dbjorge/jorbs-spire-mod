package stsjorbsmod.memories;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.util.EffectUtils;

public class PrideMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(PrideMemory.class);

    private static final int PASSIVE_DEXTERITY_LOSS = 2;

    public PrideMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
        setDescriptionPlaceholder("!M!", PASSIVE_DEXTERITY_LOSS);
    }

    @Override
    public float modifyBlock(float blockAmount) {
        if (isPassiveEffectActive) {
            return (blockAmount -= (float) PASSIVE_DEXTERITY_LOSS) < 0.0F ? 0.0F : blockAmount;
        }
        return blockAmount;
    }

    @Override
    public void onVictory() {
        if (isPassiveEffectActive) {
            CardGroup masterDeckCandidates = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade()) {
                    masterDeckCandidates.addToBottom(c);
                }
            }

            if (!masterDeckCandidates.isEmpty()) {
                AbstractCard masterDeckCard = masterDeckCandidates.getRandomCard(AbstractDungeon.cardRandomRng);
                masterDeckCard.upgrade();
                masterDeckCard.superFlash();
                EffectUtils.addPermanentCardUpgradeEffect(masterDeckCard);
            }
        }
    }
}
