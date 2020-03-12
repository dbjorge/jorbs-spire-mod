package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Goosebumps extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Goosebumps.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int MULTI = 3;
    private static final int UPGRADE_MULTI = 1;

    public Goosebumps() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = MULTI;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage)));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeMagicNumber(UPGRADE_MULTI);
            upgradeName();
            upgradeDescription();
        }
    }
}
