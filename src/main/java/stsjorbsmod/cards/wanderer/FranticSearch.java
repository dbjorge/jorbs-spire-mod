package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.actions.MakeMaterialComponentsInHandAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;

public class FranticSearch extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FranticSearch.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int MATERIAL_COMPONENTS_PER_ENEMY = 1;

    public FranticSearch() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MATERIAL_COMPONENTS_PER_ENEMY;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        int componentCount = (int) AbstractDungeon.getCurrRoom().monsters.monsters.stream().filter(m -> !m.isDeadOrEscaped()).count();
        addToBot(new MakeMaterialComponentsInHandAction(componentCount, false));
        addToBot(new GainClarityOfCurrentMemoryAction(p));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            upgradeDescription();
        }
    }
}
