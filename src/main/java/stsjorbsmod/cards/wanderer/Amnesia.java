package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RemovePowersMatchingPredicateAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

/**
 * Curse
 * - Unplayable
 * - At the end of player turn, Snap.
 * - Remove player buffs and debuffs.
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
        if(dontTriggerOnUseCard) {
            addToBot(new SnapAction(p));
            addToBot(new RemovePowersMatchingPredicateAction(p, power -> power.type == AbstractPower.PowerType.BUFF || power.type == AbstractPower.PowerType.DEBUFF));
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
