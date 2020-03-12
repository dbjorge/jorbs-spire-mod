package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ExhumeCardsAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnShuffleSubscriber;
import stsjorbsmod.characters.Cull;

public class GatheringEvil extends CustomJorbsModCard implements OnShuffleSubscriber {
    public static final String ID = JorbsMod.makeID(GatheringEvil.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int STRENGTH_PER_CURSE = 1;
    private static final int VULNERABLE_PER_CURSE = 1;

    public GatheringEvil() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = 0;
        metaMagicNumber = baseMetaMagicNumber = STRENGTH_PER_CURSE;
        urMagicNumber = baseUrMagicNumber = VULNERABLE_PER_CURSE;
        this.exhaust = true;
    }

    @Override
    public int calculateBonusMagicNumber() {
        return (int) AbstractDungeon.actionManager.cardsPlayedThisCombat.stream()
                .filter(c -> c.type.equals(CardType.CURSE))
                .count();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber * metaMagicNumber)));
        addToBot(new ApplyPowerAction(p, p, new VulnerablePower(p, magicNumber * urMagicNumber, false)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return magicNumber == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1];
    }

    @Override
    public void onShuffle() {
        if (this.upgraded) {
            addToBot(new ExhumeCardsAction(this));
        }
    }
}
