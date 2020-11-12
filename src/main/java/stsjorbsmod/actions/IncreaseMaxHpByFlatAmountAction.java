package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

import stsjorbsmod.patches.ActionShouldPersistPostCombatField;

public class IncreaseMaxHpByFlatAmountAction extends AbstractGameAction {

  private boolean showEffect;

  private int increase;

  public IncreaseMaxHpByFlatAmountAction(AbstractCreature target, AbstractCreature source, int increase,
      boolean showEffect) {
    if (Settings.FAST_MODE) {
      this.startDuration = Settings.ACTION_DUR_XFAST;
    } else {
      this.startDuration = Settings.ACTION_DUR_FAST;
    }
    this.duration = this.startDuration;
    this.showEffect = showEffect;
    this.increase = increase;
    this.source = source;
    this.target = target;
    ActionShouldPersistPostCombatField.shouldPersistPostCombat.set(this, true);
  }

  public void update() {
    if (this.duration == this.startDuration) {
      this.target.increaseMaxHp(increase, this.showEffect);
    }
    tickDuration();
  }
}