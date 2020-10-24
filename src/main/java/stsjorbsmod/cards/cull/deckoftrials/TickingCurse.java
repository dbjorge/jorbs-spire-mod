package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

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
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageType, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @Override
    public void triggerOnExhaust() {
        this.addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(magicNumber, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
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
}