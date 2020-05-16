package stsjorbsmod.cards.cull.deckoftrials;

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
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    protected int calculateBonusBaseDamage() {
        // Don't use magicNumber; it's not recalculated until *after* this is called
        return Math.min(metaMagicNumber, SkillsPlayedThisActSaveData.skillsPlayed * urMagicNumber);
    }

    @Override
    protected int calculateBonusMagicNumber() {
        return SkillsPlayedThisActSaveData.skillsPlayed;
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return magicNumber == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1];
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
