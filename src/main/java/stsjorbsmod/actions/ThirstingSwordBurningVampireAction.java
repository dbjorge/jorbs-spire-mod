package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.BurningPower;

public class ThirstingSwordBurningVampireAction extends AbstractGameAction {
    private int maxHealthReduction;

    public ThirstingSwordBurningVampireAction(AbstractCreature target, AbstractPlayer source, int maxHealthReduction) {
        this.maxHealthReduction = maxHealthReduction;
        this.target = target;
        this.source = source;
    }

    @Override
    public void update() {
        AbstractPower possibleExistingBurningPower = target.getPower(BurningPower.POWER_ID);
        if (possibleExistingBurningPower != null) {
            int healAmount = possibleExistingBurningPower.amount;
            AbstractDungeon.actionManager.addToTop(new HealAction(source, source, healAmount));
        }

        source.decreaseMaxHealth(maxHealthReduction);
        isDone = true;
    }
}
