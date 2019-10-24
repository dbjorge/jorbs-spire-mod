package stsjorbsmod.memories;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import stsjorbsmod.JorbsMod;

import java.util.ArrayList;

// We want to detect the case where a card that was just played resulted in an enemy's death.
//
// Generally, a card will queue actions to happen later, and the actions are what actually kill the
// enemy, and there is usually no direct link back from the action to the card. The two options are:
// * hook onPlayCard, and try to guess whether the card will eventually result in an enemy's death
// * hook AbstractMonster.die(), and try to guess which card (if any) was responsible
//
// The former is extremely difficult to calculate in all cases and outright impossible to calculate in a few
// special cases (particularly, if a card queues any form of DamageRandomEnemyAction, it's impossible to
// determine which enemy would be targeted before the Action executes), so we attempt the latter.
public class WrathMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(WrathMemory.class);

    private static final int DAMAGE_INCREASE_PER_KILL = 1;

    public WrathMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
        setDescriptionPlaceholder("!M!", DAMAGE_INCREASE_PER_KILL);
    }

    @Override
    public void onMonsterDeath(AbstractMonster m, DamageInfo damageInfo) {
        if (!isPassiveEffectActive || damageInfo.owner != this.owner || m.hasPower(MinionPower.POWER_ID)) {
            return;
        }

        // There doesn't exist any reliable mapping from actions back to the cards that were responsible for them,
        // so we do a best-effort guess of "the most recently played card this turn". At one point we attempted
        // checking whether the card was still "in progress" by looking for an associated UseCardAction on the
        // actions queue, but this doesn't work with cards that queue actions that queue other actions.
        ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        if (!cardsPlayed.isEmpty()) {
            AbstractCard lastCard = cardsPlayed.get(cardsPlayed.size()-1);

            this.flash();
            permanentlyIncreaseCardDamage(lastCard);
        }
    }

    // It's important that this effect *not* be implemented as an action, because if it happens in response to the
    // last enemy in a fight dying, no further actions will be executed during that fight.
    private void permanentlyIncreaseCardDamage(AbstractCard card) {
        AbstractPlayer p = AbstractDungeon.player;
        String logPrefix = "WrathMemory effect for " + card.cardID + " (" + card.uuid + "): ";

        if (card.type != AbstractCard.CardType.ATTACK) {
            JorbsMod.logger.warn(logPrefix + "Ignoring non-attack card");
            return;
        }

        if (card.baseDamage <= 0) {
            JorbsMod.logger.warn(logPrefix + "Ignoring card with <=0 baseDamage");
            return;
        }

        JorbsMod.logger.info(logPrefix + "Increasing baseDamage by " + DAMAGE_INCREASE_PER_KILL + " from " + card.baseDamage);

        AbstractCard masterCard = StSLib.getMasterDeckEquivalent(card);
        if (masterCard != null) {
            masterCard.baseDamage += DAMAGE_INCREASE_PER_KILL;
            masterCard.superFlash();
        }

        card.baseDamage += DAMAGE_INCREASE_PER_KILL;
        card.applyPowers();

        // Arguable whether to also upgrade other cards in GetAllInBattleInstances.get(card.uuid) (ie, doubled cards)
    }
}
