package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

import stsjorbsmod.patches.ActionShouldPersistPostCombatField;

public class IncreaseMaxHpByFlatAmountActionAndHealAction extends AbstractGameAction {

  private boolean showEffect;

  private int increase;
  private int heal;

  public IncreaseMaxHpByFlatAmountActionAndHealAction(AbstractCreature target, AbstractCreature source, int heal,
      int increase, boolean showEffect) {
    if (Settings.FAST_MODE) {
      this.startDuration = Settings.ACTION_DUR_XFAST;
    } else {
      this.startDuration = Settings.ACTION_DUR_FAST;
    }
    this.duration = this.startDuration;
    this.showEffect = showEffect;
    this.source = source;
    this.heal = heal;
    this.increase = increase;
    this.target = target;
    ActionShouldPersistPostCombatField.shouldPersistPostCombat.set(this, true);
  }

  public void update() {
    if (this.duration == this.startDuration) {
      this.addToBot(new HealAction(target, source, heal, duration));
      this.addToBot(new IncreaseMaxHpByFlatAmountAction(target, source, increase, showEffect));
    }
    tickDuration();
  }
}