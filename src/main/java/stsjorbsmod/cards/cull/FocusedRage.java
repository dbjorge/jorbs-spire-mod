package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.ExertedField;
import stsjorbsmod.patches.SelfExertField;

public class FocusedRage extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FocusedRage.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int STRENGTH_PER_EXERT = 2;
    private static final int UPGRADE_PLUS_STRENGTH_PER_EXERT = 1;

    public FocusedRage() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = 0;
        urMagicNumber = baseUrMagicNumber = STRENGTH_PER_EXERT;
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (magicNumber > 0) {
            addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber)));
        }
        else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, EXTENDED_DESCRIPTION[1], true));
        }
    }

    @Override
    protected int calculateBonusMagicNumber() {
        int bonusMagicNumber = 0;
        for(AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            if (ExertedField.exerted.get(c) && !ExertedField.exertedAtStartOfCombat.get(c)) {
                bonusMagicNumber += urMagicNumber;
            }
        }
        return bonusMagicNumber;
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeUrMagicNumber(UPGRADE_PLUS_STRENGTH_PER_EXERT);
            upgradeDescription();
        }
    }
}
