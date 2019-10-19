package stsjorbsmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import stsjorbsmod.actions.configuration.ActionConfiguration;

public abstract class ConfiguredJorbsModCard extends CustomJorbsModCard {
	
	private boolean baseCalculationDependsOnTarget;
	
	private ActionConfiguration actionConfiguration;

	public ConfiguredJorbsModCard(final String id,
            final String img,
            final int cost,
            final CardType type,
            final CardColor color,
            final CardRarity rarity,
            final CardTarget target) {
		this(id, img, cost, type, color, rarity, target, true);
	}
	
	protected ConfiguredJorbsModCard(final String id,
            final String img,
            final int cost,
            final CardType type,
            final CardColor color,
            final CardRarity rarity,
            final CardTarget target,
            boolean baseCalculationDependsOnTarget) {
		super(id, img, cost, type, color, rarity, target);
		this.baseCalculationDependsOnTarget = baseCalculationDependsOnTarget;
	}
	
	protected final ActionConfiguration getActionConfiguration() {
		return getActionConfiguration(null);
	}
	
	protected final ActionConfiguration getActionConfiguration(AbstractMonster mo) {
		if (baseCalculationDependsOnTarget) {
			if (actionConfiguration == null || actionConfiguration.getTempTarget() != mo) {
				if (mo == null) {
					actionConfiguration = createActionConfiguration();
				}
				else {
					actionConfiguration = createTempActionConfiguration(mo);
					actionConfiguration.setTempTarget(mo);
				}
			}
		}
		else if (actionConfiguration == null) {
			actionConfiguration = createActionConfiguration();
		}
    	return actionConfiguration;
    }
	
	protected abstract ActionConfiguration createActionConfiguration();
	
	protected abstract ActionConfiguration createTempActionConfiguration(AbstractMonster tempTarget);
    
    
    @Override
    protected int calculateBonusBaseDamage() {
    	int savedValue = actionConfiguration.getCalculatedCardDamage();
    	return savedValue - this.baseDamage;
    }
    
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
    	getActionConfiguration(mo);
    	super.calculateCardDamage(mo);
    }
    
    
    @Override
    public void applyPowers() {
    	getActionConfiguration();
    	super.applyPowers();
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	getActionConfiguration(m).enqueueActions(p, m);
    }

}
