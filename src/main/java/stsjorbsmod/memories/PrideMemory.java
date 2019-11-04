package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.util.EffectUtils;

public class PrideMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(PrideMemory.class);

    private static final int PASSIVE_DEXTERITY_MODIFIER = -2;

    public PrideMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
        setDescriptionPlaceholder("!M!", PASSIVE_DEXTERITY_MODIFIER);
    }

    @Override
    public void onGainPassiveEffect() {
        // It is by design that the dexterity decrease can be blocked by Artifact.
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DexterityPower(owner, PASSIVE_DEXTERITY_MODIFIER), PASSIVE_DEXTERITY_MODIFIER));
    }

    @Override
    public void onLosePassiveEffect() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DexterityPower(owner, -PASSIVE_DEXTERITY_MODIFIER), -PASSIVE_DEXTERITY_MODIFIER));
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
