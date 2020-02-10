package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.TImeEddyAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;

public class TimeEddy extends MaterialComponent {
    public static final String ID = JorbsMod.makeID(TimeEddy.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int POWER_COUNTER_INCREMENT = 1;
    private static final int UPGRADE_PLUS_POWER_COUNTER_INCREMENT = 1;

    public TimeEddy() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        bannerImageRarity = CardRarity.RARE;
        magicNumber = baseMagicNumber = POWER_COUNTER_INCREMENT;
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void useMaterialComponent(AbstractPlayer p, AbstractMonster m) {
        addToBot(new TImeEddyAction(p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_POWER_COUNTER_INCREMENT);
            upgradeDescription();
        }
    }
}
