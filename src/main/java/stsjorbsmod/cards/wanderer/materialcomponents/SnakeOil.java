package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.PatienceMemory;
import stsjorbsmod.powers.CoilPower;

public class SnakeOil extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(SnakeOil.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int DAMAGE_PER_COIL = 1;
    private static final int UPGRADE_PLUS_DAMAGE_PER_COIL = 1;

    public SnakeOil() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = 0;
        magicNumber = baseMagicNumber = DAMAGE_PER_COIL;
        damageType = damageTypeForTurn = DamageInfo.DamageType.THORNS;
        isMultiDamage = true;
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public int calculateBonusBaseDamage() {
        AbstractPower possibleCoilPower = AbstractDungeon.player.getPower(CoilPower.POWER_ID);
        int coilAmount = possibleCoilPower == null ? 0 : possibleCoilPower.amount;
        return coilAmount * magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, PatienceMemory.STATIC.ID));
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
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
