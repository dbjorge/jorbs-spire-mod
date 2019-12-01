package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.actions.ScorchingRayAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.LustMemory;

import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class ScorchingRay extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ScorchingRay.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int BURNING_PER_ATTACK = 3;
    private static final int NUM_ATTACKS = 3;
    private static final int UPGRADE_PLUS_NUM_ATTACKS = 1;

    public ScorchingRay() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        metaMagicNumber = baseMetaMagicNumber = BURNING_PER_ATTACK;
        magicNumber = baseMagicNumber = NUM_ATTACKS;
        tags.add(REMEMBER_MEMORY);
    }

    private void recalculateBurningAmount() {
        AbstractPower strengthPower = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
        int strength = strengthPower == null ? 0 : strengthPower.amount;
        metaMagicNumber = strength + baseMetaMagicNumber;
        isMetaMagicNumberModified = metaMagicNumber != baseMetaMagicNumber;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        recalculateBurningAmount();
    }

    @Override
    public void calculateCardDamage(AbstractMonster m) {
        super.calculateCardDamage(m);
        recalculateBurningAmount();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, LustMemory.STATIC.ID));
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ScorchingRayAction(m, p, metaMagicNumber));
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
