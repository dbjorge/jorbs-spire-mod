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
        damage = baseDamage = DAMAGE_PER_INTANGIBLE;
        urMagicNumber = baseUrMagicNumber = SELF_INTANGIBLE;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    // After calling this:
    //     * damage will still be the non-multiplied damage (for use with card description)
    //     * magicNumber will be multiplied damage (for use with card description dynamic suffix)
    //     * multiDamage will contain multiplied damage (for application to monsters)
    //
    // We aren't using calculateBonusBaseDamage because the most intuitive way for the card to display
    // is for the "per intangible" damage to actually be the "base damage" for purposes of order of operations
    // with strength/wrath/etc.
    private void recalculateTotalDamage() {
        AbstractPower possibleIntangiblePower = AbstractDungeon.player.getPower(IntangiblePlayerPower.POWER_ID);
        int intangibleStacks = possibleIntangiblePower == null ? 0 : possibleIntangiblePower.amount;
        intangibleStacks += urMagicNumber; // Account for the one we'll be adding

        baseMagicNumber = baseDamage * intangibleStacks;
        magicNumber = damage * intangibleStacks;
        isMagicNumberModified = isDamageModified || intangibleStacks > urMagicNumber;

        for(int i = 0; i < multiDamage.length; ++i) {
            multiDamage[i] = multiDamage[i] * intangibleStacks;
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        recalculateTotalDamage();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        recalculateTotalDamage();
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
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeDescription();
        }
    }
}
