package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

public class Frustration extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Frustration.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 15;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int DAMAGE_ON_RETAIN = 2;
    private static final int UPGRADE_DAMAGE_ON_RETAIN = 1;


    public Frustration() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.selfRetain = true;
        this.isMultiDamage = true;
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = DAMAGE_ON_RETAIN;
        this.exhaust = true;
        SelfExertField.selfExert.set(this, true);
    }


    @Override
    public void onRetained() {
        this.addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(magicNumber, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDescription();
        upgradeDamage(UPGRADE_DAMAGE);
        upgradeMagicNumber(UPGRADE_DAMAGE_ON_RETAIN);
    }
}
