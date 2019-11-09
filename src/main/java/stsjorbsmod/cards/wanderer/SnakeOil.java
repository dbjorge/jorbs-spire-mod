package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.PatienceMemory;
import stsjorbsmod.powers.CoilPower;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class SnakeOil extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(SnakeOil.class.getSimpleName());
    public static final String IMG = makeCardPath("Material_Components/snake_oil.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE_PER_COIL = 1;
    private static final int UPGRADE_PLUS_DAMAGE_PER_COIL = 1;

    public SnakeOil() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = 0;
        magicNumber = baseMagicNumber = DAMAGE_PER_COIL;
        isMultiDamage = true;
        retain = true;
        exhaust = true;
    }

    @Override
    public int calculateBonusBaseDamage() {
        AbstractPower possibleCoilPower = AbstractDungeon.player.getPower(CoilPower.POWER_ID);
        return possibleCoilPower == null ? 0 : possibleCoilPower.amount * magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToTop(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_PER_COIL);
            upgradeDescription();
        }
    }
}
