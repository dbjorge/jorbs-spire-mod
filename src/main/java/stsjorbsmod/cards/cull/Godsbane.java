package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;


public class Godsbane extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Godsbane.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE_PERCENT = 8;
    private static final int UPGRADE_PLUS_DAMAGE_PERCENT = 2;

    public Godsbane() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DAMAGE_PERCENT;
        damage = baseDamage = 0;
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        damage = (int)(m.maxHealth * ((double) magicNumber / 100));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_PERCENT);
            upgradeName();
            upgradeDescription();
        }
    }
}