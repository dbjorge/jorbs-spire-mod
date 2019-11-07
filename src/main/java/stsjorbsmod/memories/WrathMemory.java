package stsjorbsmod.memories;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.WrathField;
import stsjorbsmod.util.EffectUtils;

public class WrathMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(WrathMemory.class);

    private static final int DAMAGE_INCREASE_PER_KILL = 1;

    private static boolean isUpgradeCandidate(AbstractCard c) {
        return c.type == CardType.ATTACK && c.baseDamage > 0;
    }

    public static void reapplyToDeck(CardGroup deck) {
        for (AbstractCard card : deck.group) {
            final int upgradeCount = WrathField.effectCount.get(card);
            if (!isUpgradeCandidate(card) && (upgradeCount > 0)) {
                JorbsMod.logger.error("Wrath upgrade count modified for an ineligible card");
            }
            card.baseDamage += DAMAGE_INCREASE_PER_KILL * upgradeCount;
        }
    }

    public WrathMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
        setDescriptionPlaceholder("!M!", DAMAGE_INCREASE_PER_KILL);
        setCardDescriptionPlaceholder(getCardToUpgrade());
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        setCardDescriptionPlaceholder(isUpgradeCandidate(card) ? card : getCardToUpgrade());
    }

    private void setCardDescriptionPlaceholder(AbstractCard c) {
        String text = c != null ? c.name : "none";
        setDescriptionPlaceholder("!C!", text);
    }

    private AbstractCard getCardToUpgrade() {
        for (int i = AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1; i >= 0; --i) {
            AbstractCard candidate = AbstractDungeon.actionManager.cardsPlayedThisCombat.get(i);
            if (isUpgradeCandidate(candidate)) {
                return candidate;
            }
        }

        return null;
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (!isPassiveEffectActive || m.hasPower(MinionPower.POWER_ID)) {
            return;
        }

        AbstractCard cardToUpgrade = getCardToUpgrade();
        if (cardToUpgrade != null) {
            this.flash();
            permanentlyIncreaseCardDamage(cardToUpgrade);
        }
    }

    // It's important that this effect *not* be implemented as an action, because if it happens in response to the
    // last enemy in a fight dying, no further actions will be executed during that fight.
    private void permanentlyIncreaseCardDamage(AbstractCard card) {
        AbstractPlayer p = AbstractDungeon.player;
        String logPrefix = "WrathMemory effect for " + card.cardID + " (" + card.uuid + "): ";

        if (card.type != CardType.ATTACK) {
            JorbsMod.logger.warn(logPrefix + "Ignoring non-attack card");
            return;
        }

        if (card.baseDamage <= 0) {
            JorbsMod.logger.warn(logPrefix + "Ignoring card with <=0 baseDamage");
            return;
        }

        JorbsMod.logger.info(logPrefix + "Increasing baseDamage by " + DAMAGE_INCREASE_PER_KILL + " from " + card.baseDamage);

        AbstractCard cardToShowForVfx = card;
        AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
        if (masterCard != null) {
            masterCard.baseDamage += DAMAGE_INCREASE_PER_KILL;
            WrathField.effectCount.set(masterCard, WrathField.effectCount.get(masterCard) + 1);
            masterCard.superFlash();
            cardToShowForVfx = masterCard;
        }

        for (AbstractCard instance : GetAllInBattleInstances.get(card.uuid)) {
            instance.baseDamage += DAMAGE_INCREASE_PER_KILL;
            WrathField.effectCount.set(instance, WrathField.effectCount.get(instance) + 1);
            instance.applyPowers();
        }

        EffectUtils.addPermanentCardUpgradeEffect(cardToShowForVfx);
    }
}
