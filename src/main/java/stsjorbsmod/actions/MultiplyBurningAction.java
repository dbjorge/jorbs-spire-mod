package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.powers.BurningPower;

public class MultiplyBurningAction extends AbstractGameAction {
    private final int multiplier;

    public MultiplyBurningAction(AbstractMonster target, AbstractPlayer source, int multiplier) {
        this.multiplier = multiplier;
        this.target = target;
        this.source = source;
    }

    @Override
    public void update() {
        AbstractPower possibleExistingBurningPower = target.getPower(BurningPower.POWER_ID);
        if (possibleExistingBurningPower != null) {
            int stacksToAdd = possibleExistingBurningPower.amount * (multiplier - 1);
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, source, new BurningPower(target, source, stacksToAdd), stacksToAdd));
        }
        isDone = true;
    }
}
