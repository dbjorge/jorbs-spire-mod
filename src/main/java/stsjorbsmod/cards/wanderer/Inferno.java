package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.MultiplyBurningAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.LustMemory;
import stsjorbsmod.patches.SelfExhumeFields;

import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class Inferno extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Inferno.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int BURNING_MULTIPLIER = 2;
    private static final int UPGRADE_PLUS_BURNING_MULTIPLIER = 1;

    public Inferno() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BURNING_MULTIPLIER;
        exhaust = true;
        SelfExhumeFields.selfExhumeOnSnap.set(this, true);
        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, LustMemory.STATIC.ID));
        addToBot(new MultiplyBurningAction(m, p, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURNING_MULTIPLIER);
            upgradeDescription();
        }
    }

}
