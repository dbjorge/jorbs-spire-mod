package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.unique.RemoveAllPowersAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

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
        addToBot(new RemoveAllPowersAction(p, upgraded));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
