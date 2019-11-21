package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;

import java.util.List;
import java.util.stream.Collectors;

import static stsjorbsmod.JorbsMod.makeCardPath;

/**
 * Curse
 * - Unplayable
 * - At the end of player turn, lose random buff
 */
public class Amnesia extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Amnesia.class.getSimpleName());
    public static final String IMG = makeCardPath("Bad_Rares/amnesia.png");

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = COST_UNPLAYABLE;

    public Amnesia() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster _) {
        if (dontTriggerOnUseCard) {
            List<AbstractPower> buffs = p.powers.stream()
                    .filter(power -> power.type == AbstractPower.PowerType.BUFF)
                    .collect(Collectors.toList());
            if (buffs.size() == 0) {
                int index = AbstractDungeon.cardRandomRng.random(EXTENDED_DESCRIPTION.length - 1);
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, EXTENDED_DESCRIPTION[index], 2.0f, 2.0f));
            } else if (p.hasPower(ArtifactPower.POWER_ID)) {
                CardCrawlGame.sound.play("NULLIFY_SFX");
                p.getPower(ArtifactPower.POWER_ID).flashWithoutSound();
                p.getPower(ArtifactPower.POWER_ID).onSpecificTrigger();
            } else {
                int index = AbstractDungeon.cardRandomRng.random(buffs.size() - 1);
                AbstractPower powerToRemove = buffs.get(index);
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, powerToRemove.ID));
            }
            AbstractDungeon.actionManager.addToBottom(new WaitAction(2.0f));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public void upgrade() {
    }

    @Override
    public AbstractCard makeCopy() {
        return new Amnesia();
    }
}
