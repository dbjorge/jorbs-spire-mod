package stsjorbsmod.cards;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.*;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class ArcaneForm extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ArcaneForm.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Rares/arcane_form.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 3;
    private static final int UPGRADED_COST = 2;

    public ArcaneForm() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);

        tags.add(REMEMBER_MEMORY);
        tags.add(BaseModCardTags.FORM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new RememberSpecificMemoryAction(new WrathMemory(p, true)));
        enqueueAction(new RememberSpecificMemoryAction(new LustMemory(p, true)));
        enqueueAction(new RememberSpecificMemoryAction(new DiligenceMemory(p, true)));
        enqueueAction(new RememberSpecificMemoryAction(new KindnessMemory(p, true)));
        enqueueAction(new RememberSpecificMemoryAction(new ChastityMemory(p, true)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
