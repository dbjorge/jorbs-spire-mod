package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.patches.EphemeralField;

public class GrimDirge extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(GrimDirge.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 30;
    private static final int UPGRADE_DAMAGE = 10;
    private static final int TURN_EXHUMED = 3;


    public GrimDirge() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        EntombedField.entombed.set(this, true);
        EphemeralField.ephemeral.set(this, true);
        baseDamage = DAMAGE;
        baseMagicNumber = TURN_EXHUMED;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_VERTICAL));

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeName();
            upgradeDescription();
        }
    }
}
