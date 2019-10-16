package stsjorbsmod.memories;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.PermanentlyIncreaseCardDamageAction;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class WrathMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(WrathMemory.class);

    private static final int DAMAGE_INCREASE_PER_KILL = 1;

    public WrathMemory(final AbstractCreature owner, boolean isClarified) {
        super(STATIC, MemoryType.SIN, owner, isClarified);
        this.descriptionPlaceholders.put("!M!", DAMAGE_INCREASE_PER_KILL+"");
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster target) {
        if (card.type != AbstractCard.CardType.ATTACK || target.hasPower(MinionPower.POWER_ID)) {
            return;
        }

        card.calculateCardDamage(target);
        if (card.damage >= target.currentHealth) {
            JorbsMod.logger.info("Wrath: increasing damage");
            AbstractDungeon.actionManager.addToTop(
                    new PermanentlyIncreaseCardDamageAction(card.uuid, DAMAGE_INCREASE_PER_KILL));
        }
    }
}
