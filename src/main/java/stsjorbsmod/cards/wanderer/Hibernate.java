package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.SlothMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class Hibernate extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Hibernate.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Uncommons/hibernate.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 2;
    private static final int BLOCK = 20;
    private static final int UPGRADE_SNAP_INTANGIBLE = 1;

    public Hibernate() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        magicNumber = baseMagicNumber = 0;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new RememberSpecificMemoryAction(p, SlothMemory.STATIC.ID));

        MemoryManager memoryManager = MemoryManager.forPlayer(p);
        if (upgraded && memoryManager != null && memoryManager.isSnapped()) {
            addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, 1), 1));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_SNAP_INTANGIBLE);
            upgradeDescription();
        }
    }
}
