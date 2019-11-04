package stsjorbsmod.cards.wanderer;

import basemod.devcommands.draw.Draw;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.ErodeAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.TemperanceMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class Erode extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Erode.class.getSimpleName());
    public static final String IMG = makeCardPath("AoE_Uncommons/erode.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int BLOCK_REMOVAL = 10;
    private static final int POISON = 5;
    private static final int DRAW = 0;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public Erode() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BLOCK_REMOVAL;
        metaMagicNumber = baseMetaMagicNumber = POISON;
        urMagicNumber = baseUrMagicNumber = DRAW;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ErodeAction(m, p, magicNumber, metaMagicNumber));
        addToBot(new RememberSpecificMemoryAction(new TemperanceMemory(p, false)));
        if (urMagicNumber > 0) {
            addToBot(new DrawCardAction(p, urMagicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeUrMagicNumber(UPGRADE_PLUS_DRAW);
            upgradeDescription();
        }
    }
}
