package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RemovePowersMatchingPredicateAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

/**
 * TODO move to curse
 */
public class Amnesia extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Amnesia.class.getSimpleName());
    public static final String IMG = makeCardPath("Bad_Rares/amnesia.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;

    public Amnesia() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster _) {
        // Order matters here, since Clarities are technically powers and we want Snap to consider them.
        addToBot(new SnapAction(p));
        addToBot(new RemovePowersMatchingPredicateAction(p, power -> power.type == AbstractPower.PowerType.BUFF || power.type == AbstractPower.PowerType.DEBUFF));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
