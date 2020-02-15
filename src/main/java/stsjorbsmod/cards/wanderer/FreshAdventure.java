package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ConsumerGameAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.DiligenceMemory;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;
import stsjorbsmod.memories.WrathMemory;

import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

// Deal 12(15) damage and remember Diligence
public class FreshAdventure extends CustomJorbsModCard implements OnModifyMemoriesSubscriber {
    public static final String ID = JorbsMod.makeID(FreshAdventure.class);

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 14;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int WRATH_STACK_ON_SNAP = 1;

    public FreshAdventure() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = WRATH_STACK_ON_SNAP;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SLASH_DIAGONAL));
        addToBot(new RememberSpecificMemoryAction(p, DiligenceMemory.STATIC.ID));
    }

    @Override
    public void onSnap() {
        addToBot(new ConsumerGameAction<>(WrathMemory::permanentlyIncreaseCardDamage, this));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeDescription();
        }
    }
}
