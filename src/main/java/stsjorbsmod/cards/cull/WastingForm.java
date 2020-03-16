package stsjorbsmod.cards.cull;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.powers.WastingFormPower;

public class WastingForm extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(WastingForm.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 3;
    private static final int NUMBER_OF_CURSES = 5;
    private static final int DAMAGE_PER_CURSE = 12;
    private static final int UPGRADE_DAMAGE_PER_CURSE = 4;

    public WastingForm() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = DAMAGE_PER_CURSE;
        metaMagicNumber = baseMetaMagicNumber = NUMBER_OF_CURSES;

        tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < metaMagicNumber; i++) {
            AbstractCard c = AbstractDungeon.returnRandomCurse();
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(c, 1));
        }
        addToBot(new ApplyPowerAction(p, p, new WastingFormPower(p, magicNumber, metaMagicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE_PER_CURSE);
            upgradeDescription();
        }
    }
}
