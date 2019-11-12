package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.SnappedPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Mindworm extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Mindworm.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Uncommons/mindworm.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DAMAGE = 1;
    private static final int BONUS_SNAPPED_DAMAGE = 8;
    private static final int UPGRADE_PLUS_BONUS_SNAPPED_DAMAGE = 7;

    public Mindworm() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BONUS_SNAPPED_DAMAGE;
    }

    @Override
    protected int calculateBonusBaseDamage() {
        return AbstractDungeon.player.hasPower(SnappedPower.POWER_ID) ? this.magicNumber : 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.FIRE));
        addToBot(new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_BONUS_SNAPPED_DAMAGE);
            upgradeDescription();
        }
    }
}
