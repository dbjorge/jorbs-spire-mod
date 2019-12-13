package stsjorbsmod.cards.wanderer;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;

public class Defend_Wanderer extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Defend_Wanderer.class);

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public Defend_Wanderer() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;

        this.tags.add(BaseModCardTags.BASIC_DEFEND);

        try {
            this.tags.add(CardTags.valueOf("STARTER_DEFEND"));
        } catch(IllegalArgumentException e) {
            // main branch doesn't have this tag yet; intentionally ignoring
        }
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
