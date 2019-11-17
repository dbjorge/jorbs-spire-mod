package stsjorbsmod.cards.wanderer.materialcomponents;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.LoseRandomClarityAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.patches.ExtraCopiesToAddWhenGeneratingCardField;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Rot extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Rot.class.getSimpleName());
    public static final String IMG = makeCardPath("Material_Components/rot.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int EXTRA_COPIES_ON_ADD = 1;
    private static final int UPGRADE_PLUS_EXTRA_COPIES_ON_ADD = 1;

    public Rot() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EXTRA_COPIES_ON_ADD;
        ExtraCopiesToAddWhenGeneratingCardField.field.set(this, magicNumber);
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
