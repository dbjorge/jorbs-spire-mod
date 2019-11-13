package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.actions.ScorchingRayAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.LustMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class ScorchingRay extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ScorchingRay.class.getSimpleName());
    public static final String IMG = makeCardPath("placeholder.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 1;
    private static final int NUM_ATTACKS = 3;
    private static final int UPGRADE_PLUS_NUM_ATTACKS = 1;

    public ScorchingRay() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = NUM_ATTACKS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, LustMemory.STATIC.ID));
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ScorchingRayAction(m, p, damage));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_NUM_ATTACKS);
            upgradeDescription();
        }
    }
}
