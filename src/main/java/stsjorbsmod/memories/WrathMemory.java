package stsjorbsmod.memories;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.OnWrathStackReceivedSubscriber;
import stsjorbsmod.patches.WrathField;
import stsjorbsmod.util.EffectUtils;

public class WrathMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(WrathMemory.class);

    private static final int DAMAGE_INCREASE_PER_KILL = 1;

    // We ignore -1 baseDamage (the AbstractCard default), but allow 0 baseDamage (for cards like Mania)]n
    public static boolean isUpgradeCandidate(AbstractCard c) {
        return c.type == CardType.ATTACK && c.baseDamage >= 0;
    }

    public static void reapplyToLoadedCard(AbstractCard card, int effectCount) {
        if (!isUpgradeCandidate(card) && (effectCount > 0)) {
            JorbsMod.logger.error("Wrath effect count modified for an ineligible card");
            return;
        }

        WrathField.wrathEffectCount.set(card, effectCount);

        if (!WrathField.usesMiscToTrackPermanentBaseDamage(card)) {
            card.baseDamage += DAMAGE_INCREASE_PER_KILL * effectCount;
        }
    }

    private AbstractCard upgradeTarget;

    public WrathMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.SIN, owner);
        setDescriptionPlaceholder("!M!", DAMAGE_INCREASE_PER_KILL);
        setDescriptionPlaceholder("!C!", "none");
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        // Intentionally tracking upgradeTarget changes even when passive effect is inactive
        if (isUpgradeCandidate(card)) {
            upgradeTarget = card;
            setDescriptionPlaceholder("!C!", card.name);
        }
    }

    @Override
    public void onMonsterKilled(AbstractMonster m) {
        if (isPassiveEffectActive() && !m.hasPower(MinionPower.POWER_ID) && upgradeTarget != null) {
            this.flash();
            permanentlyIncreaseCardDamage(upgradeTarget);
            if (upgradeTarget instanceof OnWrathStackReceivedSubscriber) {
                ((OnWrathStackReceivedSubscriber) upgradeTarget).onWrathStackReceived();
            }
        }
    }

    // It's important that this effect *not* be implemented as an action, because if it happens in response to the
    // last enemy in a fight dying, no further actions will be executed during that fight.
    public static void permanentlyIncreaseCardDamage(AbstractCard card) {
        AbstractPlayer p = AbstractDungeon.player;
        String logPrefix = "Wrath: permanentlyIncreaseCardDamage: " + card.cardID + " (" + card.uuid + "): ";

        if (!isUpgradeCandidate(card)) {
            JorbsMod.logger.error(logPrefix + "attempting to upgrade non-upgrade-candidate?");
            return;
        }

        JorbsMod.logger.info(logPrefix + "Increasing baseDamage by " + DAMAGE_INCREASE_PER_KILL + " from " + card.baseDamage);

        AbstractCard cardToShowForVfx = card;
        AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
        if (masterCard != null) {
            WrathField.updateCardDamage(masterCard, DAMAGE_INCREASE_PER_KILL);
            WrathField.wrathEffectCount.set(masterCard, WrathField.wrathEffectCount.get(masterCard) + 1);
            masterCard.superFlash();
            cardToShowForVfx = masterCard;
        }

        for (AbstractCard instance : GetAllInBattleInstances.get(card.uuid)) {
            WrathField.updateCardDamage(instance, DAMAGE_INCREASE_PER_KILL);
            WrathField.wrathEffectCount.set(instance, WrathField.wrathEffectCount.get(instance) + 1);
            instance.applyPowers();
        }

        EffectUtils.addWrathCardUpgradeEffect(cardToShowForVfx);
    }

}
