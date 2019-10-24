package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.VampireDamageRandomEnemyAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class OldFriends extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(OldFriends.class.getSimpleName());
    public static final String IMG = makeCardPath("AOE_Rares/old_friends.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY; // random enemy
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 3;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 1;
    private static final int POISON = 12;
    private static final int UPGRADE_PLUS_POISON = 3;

    public OldFriends() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = POISON;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new VampireDamageRandomEnemyAction(this, AttackEffect.SLASH_DIAGONAL));
        enqueueAction(new ApplyPowerToRandomEnemyAction(p, new PoisonPower(null, p, magicNumber), magicNumber));
        enqueueAction(new ApplyPowerAction(p, p, new EquilibriumPower(p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_POISON);
            initializeDescription();
        }
    }
}
