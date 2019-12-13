package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.LoseRandomClarityAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.patches.ExtraCopiesToAddWhenGeneratingCardField;

public class Rot extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Rot.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int EXTRA_COPIES_ON_ADD = 1;
    private static final int UPGRADE_PLUS_EXTRA_COPIES_ON_ADD = 1;

    public Rot() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        bannerImageRarity = CardRarity.UNCOMMON;
        magicNumber = baseMagicNumber = EXTRA_COPIES_ON_ADD;
        ExtraCopiesToAddWhenGeneratingCardField.field.set(this, magicNumber);
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new LoseRandomClarityAction(p));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EXTRA_COPIES_ON_ADD);
            ExtraCopiesToAddWhenGeneratingCardField.field.set(this, magicNumber);
            upgradeDescription();
        }
    }
}
