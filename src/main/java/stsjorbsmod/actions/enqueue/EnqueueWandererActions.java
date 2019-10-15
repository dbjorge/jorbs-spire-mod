package stsjorbsmod.actions.enqueue;

import stsjorbsmod.util.MemoryPowerUtils;

public class EnqueueWandererActions extends EnqueueActions {
	
	private int clarityMultiplier;
	private int clarityAddend;
	
	public EnqueueWandererActions applyClarityMultiplier(int clarityMultiplier) {
		this.clarityMultiplier = clarityMultiplier;
		return this;
	}
	
	public EnqueueWandererActions applyClarityAddend(int clarityAddend) {
		this.clarityAddend = clarityAddend;
		return this;
	}
	
	public EnqueueWandererActions removeClarityModifiers() {
		this.clarityMultiplier = 0;
		this.clarityAddend = 0;
		return this;
	}
	
	@Override
	protected int applyTargetModifiers(int base) {
		int modified;
		if (clarityMultiplier > 0 || clarityAddend > 0) { // TODO: this currently excludes base + numClarities
			int numClarities = MemoryPowerUtils.countClarities(getSource());
			if (clarityMultiplier == 0) {
				modified = base + numClarities + clarityAddend;
			}
			else {
				modified = base * clarityMultiplier * numClarities + clarityAddend;
			}
		}
		else {
			modified = base;
		}
		return super.applySourceModifiers(modified);
	}

}
