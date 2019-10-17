package stsjorbsmod.actions.configuration;

import com.megacrit.cardcrawl.core.AbstractCreature;

import stsjorbsmod.memories.MemoryUtils;

public class WandererActionConfiguration extends ActionConfiguration {
	
	private int clarityMultiplierSource;
	private int clarityAddendSource;
	private boolean clarityAddendSourceApplied;
	private int clarityMultiplierTarget;
	private int clarityAddendTarget;
	private boolean clarityAddendTargetApplied;
	
	public WandererActionConfiguration() {
		super();
	}
	
	public WandererActionConfiguration(AbstractCreature target) {
		super(target);
	}
	
	public WandererActionConfiguration(AbstractCreature source, AbstractCreature target) {
		super(source, target);
	}
	
	public WandererActionConfiguration applyClarityMultiplicatively() {
		return applyClarityMultiplicatively(1);
	}
	
	public WandererActionConfiguration applyClarityMultiplicatively(int clarityMultiplier) {
		this.clarityMultiplierSource = clarityMultiplier;
		this.clarityMultiplierTarget = clarityMultiplier;
		return this;
	}
	
	public WandererActionConfiguration applyClarityAdditively() {
		return applyClarityAdditively(0);
	}
	
	public WandererActionConfiguration applyClarityAdditively(int clarityAddend) {
		this.clarityAddendSource = clarityAddend;
		this.clarityAddendTarget = clarityAddend;
		this.clarityAddendSourceApplied = true;
		this.clarityAddendTargetApplied = true;
		return this;
	}
	
	public WandererActionConfiguration applyClarityToSourceMultiplicatively() {
		return applyClarityToSourceMultiplicatively(1);
	}
	
	public WandererActionConfiguration applyClarityToSourceMultiplicatively(int clarityMultiplierSource) {
		this.clarityMultiplierSource = clarityMultiplierSource;
		return this;
	}
	
	public WandererActionConfiguration applyClarityToSourceAdditively() {
		return applyClarityToSourceAdditively(0);
	}
	
	public WandererActionConfiguration applyClarityToSourceAdditively(int clarityAddendSource) {
		this.clarityAddendSource = clarityAddendSource;
		this.clarityAddendSourceApplied = true;
		return this;
	}
	
	public WandererActionConfiguration applyClarityToTargetMultiplicatively() {
		return applyClarityMultiplicatively(1);
	}
	
	public WandererActionConfiguration applyClarityToTargetMultiplicatively(int clarityMultiplierTarget) {
		this.clarityMultiplierTarget = clarityMultiplierTarget;
		return this;
	}
	
	public WandererActionConfiguration applyClarityToTargetAdditively() {
		return applyClarityAdditively(0);
	}
	
	public WandererActionConfiguration applyClarityToTargetAdditively(int clarityAddendTarget) {
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
		return super.applyTargetModifiers(modified);
	}

}
