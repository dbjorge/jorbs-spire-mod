package stsjorbsmod.actions.configuration;

import static stsjorbsmod.JorbsMod.makeCardPath;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;

import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.TargetIndependentConfiguredJorbsModCard;
import stsjorbsmod.cards.WeightOfMemory;
import stsjorbsmod.characters.Wanderer;

public class ConfiguredWeightOfMemory extends TargetIndependentConfiguredJorbsModCard {
	
	public static final String ID = JorbsMod.makeID(WeightOfMemory.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Commons/weight_of_memory.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int DAMAGE = 12;
    private static final int DAMAGE_PER_REMEMBER_CARD = 2;
    private static final int UPGRADE_PLUS_DAMAGE_PER_REMEMBER_CARD = 2;
	
    public ConfiguredWeightOfMemory() {
    	super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_PER_REMEMBER_CARD;
	}

	@Override
	protected ActionConfiguration createActionConfiguration() {
		return new WandererActionConfiguration()
				.multiplyWithNumberOfRememberMemoryCards(this.magicNumber)
				.calculateAndDealDamageToTarget(baseDamage, null, AttackEffect.BLUNT_HEAVY);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_PER_REMEMBER_CARD);
            initializeDescription();
        }
	}

}
