package stsjorbsmod.actions.configuration;

import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import stsjorbsmod.memories.MemoryUtils;

public class WandererActionConfiguration extends ActionConfiguration {
	
	private int clarityMultiplierSource;
	private int clarityAddendSource;
	private boolean clarityAddendSourceApplied;
	private int clarityMultiplierTarget;
	private int clarityAddendTarget;
	private boolean clarityAddendTargetApplied;
	
	private int rememberMemoryCardMultiplierSource;
	private int rememberMemoryCardMultiplierTarget;
	
	public WandererActionConfiguration() {
		super();
	}
	
	public WandererActionConfiguration(AbstractCreature target) {
		super(target);
	}
	
	public WandererActionConfiguration(AbstractCreature source, AbstractCreature target) {
		super(source, target);
	}
	
	public WandererActionConfiguration multiplyWithClarity() {
		return multiplyWithClarity(1);
	}
	
	public WandererActionConfiguration multiplyWithClarity(int clarityMultiplier) {
		this.clarityMultiplierSource = clarityMultiplier;
		this.clarityMultiplierTarget = clarityMultiplier;
		return this;
	}
	
	public WandererActionConfiguration addClarity() {
		return addClarity(0);
	}
	
	public WandererActionConfiguration addClarity(int clarityAddend) {
		this.clarityAddendSource = clarityAddend;
		this.clarityAddendTarget = clarityAddend;
		this.clarityAddendSourceApplied = true;
		this.clarityAddendTargetApplied = true;
		return this;
	}
	
	public WandererActionConfiguration multiplySourceValueWithClarity() {
		return multiplySourceValueWithClarity(1);
	}
	
	public WandererActionConfiguration multiplySourceValueWithClarity(int clarityMultiplierSource) {
		this.clarityMultiplierSource = clarityMultiplierSource;
		return this;
	}
	
	public WandererActionConfiguration addClarityToSourceValue() {
		return addClarityToSourceValue(0);
	}
	
	public WandererActionConfiguration addClarityToSourceValue(int clarityAddendSource) {
		this.clarityAddendSource = clarityAddendSource;
		this.clarityAddendSourceApplied = true;
		return this;
	}
	
	public WandererActionConfiguration multiplyTargetValueWithClarity() {
		return multiplyWithClarity(1);
	}
	
	public WandererActionConfiguration multiplyTargetValueWithClarity(int clarityMultiplierTarget) {
		this.clarityMultiplierTarget = clarityMultiplierTarget;
		return this;
	}
	
	public WandererActionConfiguration addClarityToTargetValue() {
		return addClarity(0);
	}
	
	public WandererActionConfiguration addClarityToTargetValue(int clarityAddendTarget) {
		this.clarityAddendTarget = clarityAddendTarget;
		this.clarityAddendTargetApplied = true;
		return this;
	}
	
	public WandererActionConfiguration removeClarityModifiers() {
		removeClarityModifiersSource();
		removeClarityModifiersTarget();
		return this;
	}
	
	public WandererActionConfiguration removeClarityModifiersSource() {
		this.clarityMultiplierSource = 0;
		this.clarityAddendSource = 0;
		this.clarityAddendSourceApplied = false;
		return this;
	}
	
	public WandererActionConfiguration removeClarityModifiersTarget() {
		this.clarityMultiplierTarget = 0;
		this.clarityAddendTarget = 0;
		this.clarityAddendTargetApplied = false;
		return this;
	}
	
	public WandererActionConfiguration multiplyWithNumberOfRememberMemoryCards(int multiplier) {
		this.rememberMemoryCardMultiplierSource = multiplier;
		this.rememberMemoryCardMultiplierTarget = multiplier;
		return this;
	}
	
	@Override
	protected int applySourceModifiers(int base) {
		int modified;
		if (clarityAddendSourceApplied || clarityMultiplierSource > 0) {
			int numClarities = MemoryUtils.countClarities(getSource());
			if (clarityMultiplierSource == 0) {
				modified = base + numClarities + clarityAddendSource;
			}
			else {
				modified = base * clarityMultiplierSource * numClarities + clarityAddendSource;
			}
		}
		else {
			modified = base;
		}
		if (rememberMemoryCardMultiplierSource > 0) {
			int numRememberMemoryCards = countRememberMemoryCards();
			if (numRememberMemoryCards > 0) {
				modified *= rememberMemoryCardMultiplierSource * numRememberMemoryCards;
			}
		}
		return super.applySourceModifiers(modified);
	}
	
	@Override
	protected int applyTargetModifiers(int base) {
		int modified;
		if (clarityAddendTargetApplied || clarityMultiplierTarget > 0) {
			int numClarities = MemoryUtils.countClarities(getSource());
			if (clarityMultiplierTarget == 0) {
				modified = base + numClarities + clarityAddendTarget;
			}
			else {
				modified = base * clarityMultiplierTarget * numClarities + clarityAddendTarget;
			}
		}
		else {
			modified = base;
		}
		if (rememberMemoryCardMultiplierTarget > 0) {
			int numRememberMemoryCards = countRememberMemoryCards();
			if (numRememberMemoryCards > 0) {
				modified *= rememberMemoryCardMultiplierTarget * numRememberMemoryCards;
			}
		}
		return super.applyTargetModifiers(modified);
	}
	
	private int countRememberMemoryCards() {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (isRememberMemoryCard(c)) { ++count; }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (isRememberMemoryCard(c)) { ++count; }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (isRememberMemoryCard(c)) { ++count; }
        }
        return count;
    }

    private boolean isRememberMemoryCard(AbstractCard c) {
        return c.hasTag(REMEMBER_MEMORY);
    }

}
