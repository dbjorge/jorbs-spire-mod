package stsjorbsmod.cards.cull.deckoftrials;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.characters.SkillsPlayedThisActSaveData;
import stsjorbsmod.patches.SelfExertField;

import static stsjorbsmod.JorbsMod.MOD_ID;

public class Assertion extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Assertion.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 0;
    private static final int DAMAGE_PER_SKILL = 3;
    private static final int MAX_DAMAGE = 50;
    private static final int UPGRADE_MAX_DAMAGE_AMOUNT = 25;

    public Assertion() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        urMagicNumber = baseUrMagicNumber = DAMAGE_PER_SKILL;
        metaMagicNumber = baseMetaMagicNumber = MAX_DAMAGE;
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = SkillsPlayedThisActSaveData.skillsPlayed;

        SelfExertField.selfExert.set(this, true);

        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        baseMagicNumber = magicNumber = SkillsPlayedThisActSaveData.skillsPlayed;

        this.rawDescription = cardStrings.DESCRIPTION;
        if (magicNumber == 1) {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
        } else {
            this.rawDescription += cardStrings.EXTENDED_DESCRIPTION[1];
        }

        this.initializeDescription();
    }

    @Override
    protected int calculateBonusBaseDamage() {
        return Math.min(metaMagicNumber, magicNumber * urMagicNumber);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_MAX_DAMAGE_AMOUNT);
            upgradeDescription();
        }
    }
}
