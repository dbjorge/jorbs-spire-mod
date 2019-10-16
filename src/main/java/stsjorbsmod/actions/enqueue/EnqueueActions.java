package stsjorbsmod.actions.enqueue;

import java.util.ArrayList;
import java.util.List;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class EnqueueActions {
	
	private AbstractCreature source;
	private AbstractCreature target;
	
	private int defaultBaseSourceDamage;
	private int defaultBaseTargetDamage;

	private DamageType defaultSourceDamageType;
	private DamageType defaultTargetDamageType;
	
	private AttackEffect defaultSourceAttackEffect;
	private AttackEffect defaultTargetAttackEffect;
	
	private int lastCalculatedValue;
	private int savedCalculatedValue;
	
	private List<AbstractGameAction> bottomActions;
	private List<AbstractGameAction> topActions;
	
	public EnqueueActions(AbstractCreature target) {
		this(AbstractDungeon.player, target);
	}
	
	public EnqueueActions(AbstractCreature source, AbstractCreature target) {
		this.source = source;
		this.target = target;
	}
	
	public AbstractCreature getSource() {
		return source;
	}
	
	public AbstractCreature getTarget() {
		return target;
	}
	
	public int getLastCalculatedValue() {
		return lastCalculatedValue;
	}
	
	public EnqueueActions saveLastCalculatedValue() {
		this.savedCalculatedValue = lastCalculatedValue;
		return this;
	}
	
	public int getSavedCalculatedValue() {
		return savedCalculatedValue;
	}
	
	public EnqueueActions setDefaultSourceDamage(int base, DamageType type, AttackEffect effect) {
		this.defaultBaseSourceDamage = base;
		this.defaultSourceDamageType = type;
		this.defaultSourceAttackEffect = effect;
		return this;
	}
	
	public EnqueueActions setDefaultTargetDamage(int base, DamageType type, AttackEffect effect) {
		this.defaultBaseTargetDamage = base;
		this.defaultTargetDamageType = type;
		this.defaultTargetAttackEffect = effect;
		return this;
	}
	
	public EnqueueActions resetDefaultAttack() {
		resetDefaultSourceDamage();
		resetDefaultTargetDamage();
		return this;
	}
	
	public EnqueueActions resetDefaultSourceDamage() {
		this.defaultBaseSourceDamage = 0;
		this.defaultSourceDamageType = null;
		this.defaultSourceAttackEffect = null;
		return this;
	}
	
	public EnqueueActions resetDefaultTargetDamage() {
		this.defaultBaseTargetDamage = 0;
		this.defaultTargetDamageType = null;
		this.defaultTargetAttackEffect = null;
		return this;
	}
	
	public EnqueueActions calculateAndDealDamageToSource() {
		return calculateAndDealDamageToTarget(defaultBaseSourceDamage, defaultSourceDamageType, defaultSourceAttackEffect);
	}
	
	public EnqueueActions calculateAndDealDamageToSource(DamageType type, AttackEffect effect) {
		return calculateAndDealDamageToTarget(defaultBaseSourceDamage, type, effect);
	}
	
	public EnqueueActions calculateAndDealDamageToSource(int baseDamage, DamageType type, AttackEffect effect) {
		return dealBaseDamageToSource(applySourceModifiers(baseDamage), type, effect);
	}
	
	public EnqueueActions dealBaseDamageToSource(int damage) {
		return dealBaseDamageToSource(damage, defaultSourceDamageType, defaultSourceAttackEffect);
	}
	
	public EnqueueActions dealBaseDamageToSource(int damage, DamageType type, AttackEffect effect) {
		return enqueueAction(new DamageAction(source, new DamageInfo(source, damage, type), effect));
	}
	
	public EnqueueActions calculateAndDealDamageToTarget() {
		return calculateAndDealDamageToTarget(defaultBaseTargetDamage, defaultTargetDamageType, defaultTargetAttackEffect);
	}
	
	public EnqueueActions calculateAndDealDamageToTarget(DamageType type, AttackEffect effect) {
		return calculateAndDealDamageToTarget(defaultBaseTargetDamage, type, effect);
	}
	
	public EnqueueActions calculateAndDealDamageToTarget(int baseDamage, DamageType type, AttackEffect effect) {
		return dealBaseDamageToTarget(applyTargetModifiers(baseDamage), type, effect);
	}
	
	public EnqueueActions dealBaseDamageToTarget(int damage) {
		return dealBaseDamageToTarget(damage, defaultTargetDamageType, defaultTargetAttackEffect);
	}
	
	public EnqueueActions dealBaseDamageToTarget(int damage, DamageType type, AttackEffect effect) {
		return enqueueAction(new DamageAction(target, new DamageInfo(source, damage, type), effect));
	}
	
	public EnqueueActions enqueueAction(AbstractGameAction action) {
		if (bottomActions == null) {
			bottomActions = new ArrayList<>();
		}
		bottomActions.add(action);
		return this;
	}
	
	public EnqueueActions enqueueActionToTop(AbstractGameAction action) {
		if (topActions == null) {
			topActions = new ArrayList<>();
		}
		topActions.add(action);
		return this;
	}
	
	public EnqueueActions enqueueActions() {
		if (bottomActions != null) {
			for (AbstractGameAction action : bottomActions) {
				AbstractDungeon.actionManager.addToBottom(action);
			}
		}
		if (topActions != null) {
			for (AbstractGameAction action : topActions) {
				AbstractDungeon.actionManager.addToTop(action);
			}
		}
		return this;
	}
	
	protected int applySourceModifiers(int base) {
		return base;
	}
	
	protected int applyTargetModifiers(int base) {
		return base;
	}

}
