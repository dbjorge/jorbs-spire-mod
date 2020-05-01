package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ConsumeCardAction;
import stsjorbsmod.actions.DamageWithOnKillEffectAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

import static stsjorbsmod.JorbsMod.JorbsCardTags.PERSISTENT_POSITIVE_EFFECT;

public class TickingCurse extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(TickingCurse.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 11;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int EXHAUST_DAMAGE = 5;
    private static final int UPGRADE_EXHAUST_DAMAGE = 3;

    public TickingCurse() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = EXHAUST_DAMAGE;
        this.isEthereal = true;
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAllEnemiesAction(p, DAMAGE, this.damageType, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    public void triggerOnExhaust() {
        if (this.upgraded) {
            this.addToBot(new DamageAllEnemiesAction(null, EXHAUST_DAMAGE + UPGRADE_EXHAUST_DAMAGE, this.damageType, AbstractGameAction.AttackEffect.FIRE));
        } else {
            this.addToBot(new DamageAllEnemiesAction(null, EXHAUST_DAMAGE, this.damageType, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_EXHAUST_DAMAGE);
            upgradeDescription();
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // save original damage so we can calculate secondary damage number (magic number holds it)
        int origBaseDamage = this.baseDamage;
        this.baseDamage = this.baseMagicNumber;

        // calculate attack damage for secondary attack damage (magicNumber)
        super.calculateCardDamage(mo);
        int secondaryDamage = this.damage;
        boolean isSecondaryDamageModified = isDamageModified;

        // reset and recalculate the primary attack damage
        this.baseDamage = origBaseDamage;
        super.calculateCardDamage(mo);

        // set magic numbers again as they get reset from second calculateCardDamage() call
        this.magicNumber = secondaryDamage;
        this.isMagicNumberModified = isSecondaryDamageModified;
    }

    @Override
    public void applyPowers() {
        int origBaseDamage = this.baseDamage;
        this.baseDamage = this.baseMagicNumber;

        // calculate powers for secondary attack damage (magicNumber)
        super.applyPowers();
        int secondaryDamage = this.damage;
        boolean isSecondaryDamageModified = isDamageModified;

        // reset and recalculate the primary powers
        this.baseDamage = origBaseDamage;
        super.applyPowers();

        // set magic numbers again as they get reset from second calculateCardDamage() call
        this.magicNumber = secondaryDamage;
        this.isMagicNumberModified = isSecondaryDamageModified;
    }
}