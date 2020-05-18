package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RetainCardsPolitelyAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;
import stsjorbsmod.powers.ClarifyPower;

public class Clarify extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Clarify.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int CARDS_RETAINED = 1;
    private static final int UPGRADE_PLUS_CARDS_RETAINED = 1;

    public Clarify() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        selfRetain = true;
        baseMagicNumber = magicNumber = CARDS_RETAINED;
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(p, p, new ClarifyPower(p)));
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        addToBot(new RetainCardsPolitelyAction(AbstractDungeon.player, magicNumber));
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDescription();
        upgradeMagicNumber(UPGRADE_PLUS_CARDS_RETAINED);
    }
}
