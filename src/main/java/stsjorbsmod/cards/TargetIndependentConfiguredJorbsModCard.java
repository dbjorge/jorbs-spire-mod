package stsjorbsmod.cards;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

import stsjorbsmod.actions.configuration.ActionConfiguration;

public abstract class TargetIndependentConfiguredJorbsModCard extends ConfiguredJorbsModCard {
	
	public TargetIndependentConfiguredJorbsModCard(String id, String img, int cost, CardType type, CardColor color,
			CardRarity rarity, CardTarget target) {
		super(id, img, cost, type, color, rarity, target, false);
	}

	@Override
	protected final ActionConfiguration createTempActionConfiguration(AbstractMonster tempTarget) {
		return null; // this method is never called
	}

}
