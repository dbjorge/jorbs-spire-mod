package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;

import static stsjorbsmod.JorbsMod.makeCardPath;

// Deal 8 damage + 2(3) damager for each clarity
public class Firebolt extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Firebolt.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Commons/firebolt.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int DAMAGE_PER_CLARITY = 2;
    private static final int UPGRADE_PLUS_DAMAGE_PER_CLARITY = 1;

    public Firebolt() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = DAMAGE_PER_CLARITY;
    }

    @Override
    protected int calculateBonusBaseDamage() {
        return this.magicNumber * MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.FIRE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_PER_CLARITY);
            initializeDescription();
        }
    }
}
