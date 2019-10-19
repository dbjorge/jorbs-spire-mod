package stsjorbsmod.memories;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.MinionPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.PermanentlyIncreaseCardDamageAction;

public class WrathMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(WrathMemory.class);

    private static final int DAMAGE_INCREASE_PER_KILL = 1;

    public WrathMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
        setDescriptionPlaceholder("!M!", DAMAGE_INCREASE_PER_KILL);
    }

    private void activateWrathUpgrade(AbstractCard card, AbstractMonster target) {
        if (target.hasPower(MinionPower.POWER_ID)) {
            return;
        }

        // This sets a field on the card object
        card.calculateCardDamage(target);

        if (card.damage >= target.currentHealth) {
            JorbsMod.logger.info("Wrath: increasing damage");
            AbstractDungeon.actionManager.addToTop(
                    new PermanentlyIncreaseCardDamageAction(card.uuid, DAMAGE_INCREASE_PER_KILL));
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster target) {
        if (card.type != AbstractCard.CardType.ATTACK) {
            return;
        }

        if (card.target != null) {
            activateWrathUpgrade(card, target);
        } else {
            // Assuming it's a "damage ALL enemies" effect
            final MonsterGroup currentFightMonsters = AbstractDungeon.getMonsters();
            currentFightMonsters.monsters.forEach(monster -> activateWrathUpgrade(card, monster));
        }
    }
}
