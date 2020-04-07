package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

public class FalseBlessings extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FalseBlessings.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;


    public FalseBlessings() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        for (int i = 0; i < AbstractDungeon.player.potionSlots ; i++) {
            addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
        }
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDescription();
        upgradeBaseCost(0);
    }
}

