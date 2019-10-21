package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

// Deal 10(12) damage and apply 3(5) weak
public class RayOfFrost extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(RayOfFrost.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Commons/ray_of_frost.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int WEAK = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int UPGRADE_PLUS_WEAK = 2;

    public RayOfFrost() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = WEAK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.BLUNT_LIGHT));
        enqueueAction(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_WEAK);
            initializeDescription();
        }
    }
}
