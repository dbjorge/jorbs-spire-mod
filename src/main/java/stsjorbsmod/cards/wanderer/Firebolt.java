package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.BurningPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

/**
 * BASE: Deal 8 damage + 2 damage for each clarity
 * UPGRADE: Deal 8 damage. Apply 2 Burning for each clarity
 */
public class Firebolt extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Firebolt.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Commons/firebolt.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int AMOUNT_PER_CLARITY = 2;

    public Firebolt() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = metaMagicNumber = baseMetaMagicNumber = DAMAGE;
        magicNumber = baseMagicNumber = AMOUNT_PER_CLARITY;
    }

    @Override
    protected int calculateBonusBaseDamage() {
        if (upgraded) {
            return 0;
        } else {
            return this.magicNumber * MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage), AttackEffect.FIRE));
        if(upgraded) {
            int burningAmt = this.baseMagicNumber * MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities();
            if (burningAmt > 0) {
                addToBot(new ApplyPowerAction(m, p, new BurningPower(m, p, burningAmt)));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
