package stsjorbsmod.actions.enqueue;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EnqueueActions {
	
	private AbstractCreature source;
	private AbstractCreature target;
	
	private DamageType sourceDamageType;
	private DamageType targetDamageType;
	
	private AttackEffect sourceAttackEffect;
	private AttackEffect targetAttackEffect;
	
	private int baseSourceDamage;
	private int baseTargetDamage;
	
	public AbstractCreature getSource() {
		return source;
	}
	
	public AbstractCreature getTarget() {
		return target;
	}
	
	public EnqueueActions calculateAndDealDamageToSource() {
		return calculateAndDealDamageToTarget(baseSourceDamage, sourceDamageType, sourceAttackEffect);
	}
	
	public EnqueueActions calculateAndDealDamageToSource(DamageType type, AttackEffect effect) {
		return calculateAndDealDamageToTarget(baseSourceDamage, type, effect);
	}
	
	public EnqueueActions calculateAndDealDamageToSource(int baseDamage, DamageType type, AttackEffect effect) {
		return dealDamageToTarget(applySourceModifiers(baseDamage), type, effect);
	}
	
	public EnqueueActions dealDamageToSource(int damage) {
		return dealDamageToTarget(damage, sourceDamageType, sourceAttackEffect);
	}
	
	public EnqueueActions dealDamageToSource(int damage, DamageType type, AttackEffect effect) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(source, new DamageInfo(source, damage, type), effect));
		return this;
	}
	
	public EnqueueActions calculateAndDealDamageToTarget() {
		return calculateAndDealDamageToTarget(baseTargetDamage, targetDamageType, targetAttackEffect);
	}
	
	public EnqueueActions calculateAndDealDamageToTarget(DamageType type, AttackEffect effect) {
		return calculateAndDealDamageToTarget(baseTargetDamage, type, effect);
	}
	
	public EnqueueActions calculateAndDealDamageToTarget(int baseDamage, DamageType type, AttackEffect effect) {
		return dealDamageToTarget(applyTargetModifiers(baseDamage), type, effect);
	}
	
	public EnqueueActions dealDamageToTarget(int damage) {
		return dealDamageToTarget(damage, targetDamageType, targetAttackEffect);
	}
	
	public EnqueueActions dealDamageToTarget(int damage, DamageType type, AttackEffect effect) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(target, new DamageInfo(source, damage, type), effect));
		return this;
	}
	
	protected int applySourceModifiers(int base) {
		return base;
	}
	
	protected int applyTargetModifiers(int base) {
		return base;
	}

}
