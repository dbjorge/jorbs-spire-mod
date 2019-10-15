package stsjorbsmod.actions.enqueue;

import com.megacrit.cardcrawl.core.AbstractCreature;

import stsjorbsmod.util.MemoryPowerUtils;

public class EnqueueWandererActions extends EnqueueActions {
	
	private int sourceClarityMultiplier;
	private int sourceClarityAddend;
	private boolean sourceClarityAddendApplied;
	private int targetClarityMultiplier;
	private int targetClarityAddend;
	private boolean targetClarityAddendApplied;
	
	public EnqueueWandererActions(AbstractCreature target) {
		super(target);
	}
	
	public EnqueueWandererActions(AbstractCreature source, AbstractCreature target) {
		super(source, target);
	}
	
	public EnqueueWandererActions applyClarityMultiplier(int clarityMultiplier) {
		this.sourceClarityMultiplier = clarityMultiplier;
		this.targetClarityMultiplier = clarityMultiplier;
		return this;
	}
	
	public EnqueueWandererActions applyClarityAddend(int clarityAddend) {
		this.sourceClarityAddend = clarityAddend;
		this.targetClarityAddend = clarityAddend;
		this.sourceClarityAddendApplied = true;
		this.targetClarityAddendApplied = true;
		return this;
	}
	
	public EnqueueWandererActions applySourceClarityMultiplier(int sourceClarityMultiplier) {
		this.sourceClarityMultiplier = sourceClarityMultiplier;
		return this;
	}
	
	public EnqueueWandererActions applySourceClarityAddend(int sourceClarityAddend) {
		this.sourceClarityAddend = sourceClarityAddend;
		this.sourceClarityAddendApplied = true;
		return this;
	}
	
	public EnqueueWandererActions applyTargetClarityMultiplier(int targetClarityMultiplier) {
		this.targetClarityMultiplier = targetClarityMultiplier;
		return this;
	}
	
	public EnqueueWandererActions applyTargetClarityAddend(int targetClarityAddend) {
		this.targetClarityAddend = targetClarityAddend;
		this.targetClarityAddendApplied = true;
		return this;
	}
	
	public EnqueueWandererActions removeClarityModifiers() {
		removeSourceClarityModifiers();
		removeTargetClarityModifiers();
		return this;
	}
	
	public EnqueueWandererActions removeSourceClarityModifiers() {
		this.sourceClarityMultiplier = 0;
		this.sourceClarityAddend = 0;
		this.sourceClarityAddendApplied = false;
		return this;
	}
	
	public EnqueueWandererActions removeTargetClarityModifiers() {
		this.targetClarityMultiplier = 0;
		this.targetClarityAddend = 0;
		this.targetClarityAddendApplied = false;
		return this;
	}
	
	@Override
	protected int applySourceModifiers(int base) {
		int modified;
		if (sourceClarityAddendApplied || sourceClarityMultiplier > 0) {
			int numClarities = MemoryPowerUtils.countClarities(getSource());
			if (sourceClarityMultiplier == 0) {
				modified = base + numClarities + sourceClarityAddend;
			}
			else {
				modified = base * sourceClarityMultiplier * numClarities + sourceClarityAddend;
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
		if (targetClarityAddendApplied || targetClarityMultiplier > 0) {
			int numClarities = MemoryPowerUtils.countClarities(getSource());
			if (targetClarityMultiplier == 0) {
				modified = base + numClarities + targetClarityAddend;
			}
			else {
				modified = base * targetClarityMultiplier * numClarities + targetClarityAddend;
			}
		}
		else {
			modified = base;
		}
		return super.applyTargetModifiers(modified);
	}

}
