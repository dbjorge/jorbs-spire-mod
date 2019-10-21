package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.IntrospectionPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Introspection extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Introspection.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Uncommons/introspection.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int HP_LOSS = 1;
    private static final int DAMAGE = 3;
    private static final int DAMAGE_PER_CLARITY = 2;
    private static final int UPGRADE_PLUS_DAMAGE_PER_CLARITY = 1;

    public Introspection() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        damageType = damageTypeForTurn = DamageType.THORNS;
        magicNumber = baseMagicNumber = DAMAGE_PER_CLARITY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new ApplyPowerAction(p, p, new IntrospectionPower(p, HP_LOSS, baseDamage, magicNumber, damageTypeForTurn)));
    }

    @Override
    public int calculateBonusBaseDamage() {
        return magicNumber * MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_PER_CLARITY);
            upgradeDescription();
        }
    }
}
