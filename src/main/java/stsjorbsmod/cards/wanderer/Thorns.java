package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.actions.IfEnemyIntendsToAttackAction;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.HumilityMemory;
import stsjorbsmod.util.IntentUtils;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class Thorns extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Thorns.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Uncommons/thorns.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;

    public Thorns() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, HumilityMemory.STATIC.ID));
        if (upgraded) {
            addToBot(new GainClarityOfCurrentMemoryAction(p));
        } else {
            addToBot(new IfEnemyIntendsToAttackAction(new GainClarityOfCurrentMemoryAction(p)));
        }
    }

    @Override
    public boolean shouldGlowGold() {
        return !upgraded && IntentUtils.playerCanSeeThatAnyEnemyIntentMatches(IntentUtils::isAttackIntent);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
