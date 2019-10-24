package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.TemperanceMemory;
import stsjorbsmod.powers.NextAttackMissesPower;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class MirrorImage extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(MirrorImage.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Rares/mirror_image.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 2;

    public MirrorImage() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(new TemperanceMemory(p, false)));
        addToBot(new ApplyPowerAction(p, p, new NextAttackMissesPower(p)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            retain = true;
            upgradeDescription();
        }
    }
}
