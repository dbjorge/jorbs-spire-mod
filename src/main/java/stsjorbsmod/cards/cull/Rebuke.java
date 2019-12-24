package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Rebuke extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Rebuke.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE_PER_INTANGIBLE = 10;
    private static final int UPGRADE_PLUS_DAMAGE = 3;
    private static final int SELF_INTANGIBLE = 1;

    public Rebuke() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = 0;
        magicNumber = baseMagicNumber = DAMAGE_PER_INTANGIBLE;
        urMagicNumber = baseUrMagicNumber = SELF_INTANGIBLE;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    @Override
    public int calculateBonusBaseDamage() {
        AbstractPower possibleIntangiblePower = AbstractDungeon.player.getPower(IntangiblePlayerPower.POWER_ID);
        return possibleIntangiblePower == null ? magicNumber : (possibleIntangiblePower.amount + SELF_INTANGIBLE) * magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, urMagicNumber)));
        addToBot(new VFXAction(new CleaveEffect()));
        this.addToBot(new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE);
            upgradeDescription();
        }
    }
}
