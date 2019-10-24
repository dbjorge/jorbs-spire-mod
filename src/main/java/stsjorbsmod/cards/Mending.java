package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.EndTurnNowAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Mending extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Mending.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Uncommons/mending.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int HEAL_PER_CLARITY = 1;

    public Mending() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = HEAL_PER_CLARITY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int healAmount = MemoryManager.forPlayer(p).countCurrentClarities() * magicNumber;
        addToBot(new HealAction(p, p, healAmount));
        addToBot(new EndTurnNowAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.retain = true;
            upgradeDescription();
        }
    }
}
