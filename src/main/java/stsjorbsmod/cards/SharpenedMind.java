package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.SharpenedMindPower;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

// [Power, unstackable]
// 1(0): At the start of your turn, remember Patience. NL At the end of your turn, remember Diligence.
// Note: this implementation means the only effect from Diligence is the card draw. By the time Diligence
// is remembered, the end-of-turn trigger has already passed for it to retain a card. That could be changed.
public class SharpenedMind extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(SharpenedMind.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Rares/sharpened_mind.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int UPGRADE_NEW_BASE_COST = 0;

    public SharpenedMind() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new ApplyPowerAction(p, p, new SharpenedMindPower(p)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_NEW_BASE_COST);
        }
    }
}
