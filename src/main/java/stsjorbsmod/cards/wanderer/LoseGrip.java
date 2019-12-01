package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.AutoExhumeBehavior;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.SelfExhumeFields;

public class LoseGrip extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(LoseGrip.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 10;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    public LoseGrip() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        exhaust = true;
        SelfExhumeFields.selfExhumeOnSnap.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeDescription();
        }
    }
}
