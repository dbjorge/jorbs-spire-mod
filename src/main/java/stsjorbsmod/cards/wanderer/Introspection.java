package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
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
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int HP_LOSS = 1;
    private static final int DAMAGE = 2;
    private static final int DAMAGE_PER_CLARITY = 2;
    private static final int UPGRADE_PLUS_DAMAGE = 2;

    public Introspection() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DAMAGE;
        metaMagicNumber = baseMetaMagicNumber = DAMAGE_PER_CLARITY;
        urMagicNumber = baseUrMagicNumber = HP_LOSS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntrospectionPower(p, urMagicNumber, magicNumber, metaMagicNumber)));
    }

    @Override
    public int calculateBonusBaseDamage() {
        return magicNumber * MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities();
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
