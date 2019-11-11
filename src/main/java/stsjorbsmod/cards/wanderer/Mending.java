package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.PERSISTENT_POSITIVE_EFFECT;

public class Mending extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Mending.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Uncommons/mending.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int HEAL_PER_CLARITY = 1;
    private static final int UPGRADE_PLUS_HEAL_PER_CLARITY = 1;

    public Mending() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = HEAL_PER_CLARITY;
        exhaust = true;

        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int healAmount = MemoryManager.forPlayer(p).countCurrentClarities() * magicNumber;
        addToBot(new HealAction(p, p, healAmount));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_HEAL_PER_CLARITY);
        }
    }
}
