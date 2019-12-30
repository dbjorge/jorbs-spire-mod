package stsjorbsmod.memories;

public interface OnEnergyRechargeToConserveSubscriber {
    /**
     * Called when energy is recharging between turns.
     * @return false to use the default recharge behavior (reset to energyPerTurn), true to use IceCream-like behavior (add energyPerTurn)
     */
    boolean onEnergyRechargeToConserve();
}
