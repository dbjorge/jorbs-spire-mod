package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.SlothMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Mania extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Mania.class.getSimpleName());
    public static final String IMG = makeCardPath("Bad_Uncommons/mania.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BASE_SWINGS = 2;
    private static final int ADDITIONAL_SLOTH_SWINGS = 2;

    public Mania() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int numSwings = BASE_SWINGS;
        if (MemoryManager.forPlayer(p).hasMemoryOrClarity(SlothMemory.STATIC.ID)) {
            numSwings += ADDITIONAL_SLOTH_SWINGS;
        }

        for (int i=0; i<numSwings; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SLASH_VERTICAL));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
