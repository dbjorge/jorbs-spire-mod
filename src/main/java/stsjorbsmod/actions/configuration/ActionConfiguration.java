package stsjorbsmod.actions.configuration;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ActionConfiguration {
	
	private static final AbstractCreature PLAYER_PLACEHOLDER = new AbstractCreature() {

		@Override
		public void damage(DamageInfo arg0) {
			
		}

		@Override
		public void render(SpriteBatch arg0) {
			
		}
		
	};
	
	private static final AbstractCreature MONSTER_PLACEHOLDER = new AbstractCreature() {

		@Override
		public void damage(DamageInfo arg0) {
			
		}

		@Override
		public void render(SpriteBatch arg0) {
			
		}
		
	};
	
	private AbstractCreature source;
	private AbstractCreature target;
	
	private int defaultBaseSourceDamage;
	private int defaultBaseTargetDamage;

	private DamageType defaultSourceDamageType;
	private DamageType defaultTargetDamageType;
	
	private AttackEffect defaultSourceAttackEffect;
	private AttackEffect defaultTargetAttackEffect;
	
	private AbstractMonster tempTarget;
	
	private int lastCalculatedValue;
	private int savedCalculatedValue;
	
	private List<AbstractGameAction> bottomActions;
	private List<AbstractGameAction> topActions;
	
	public ActionConfiguration() {
		this(PLAYER_PLACEHOLDER, MONSTER_PLACEHOLDER);
	}
	
	public ActionConfiguration(AbstractCreature target) {
		this(AbstractDungeon.player, target);
	}
	
	public ActionConfiguration(AbstractCreature source, AbstractCreature target) {
		this.source = source;
		this.target = target;
	}
	
	public AbstractCreature getSource() {
		return source;
	}
	
	public AbstractCreature getTarget() {
		return target;
	}
	
	public void setTempTarget(AbstractMonster mo) {
		this.tempTarget = mo;
	}
	
	public AbstractMonster getTempTarget() {
		return tempTarget;
	}
	
	public int getLastCalculatedValue() {
		return lastCalculatedValue;
	}
	
	public ActionConfiguration markCalculatedCardDamage() {
		this.savedCalculatedValue = lastCalculatedValue;
		return this;
	}
	
	public int getCalculatedCardDamage() {
		return savedCalculatedValue;
	}
	
	public ActionConfiguration setDefaultSourceDamage(int base, DamageType type, AttackEffect effect) {
		this.defaultBaseSourceDamage = base;
		this.defaultSourceDamageType = type;
		this.defaultSourceAttackEffect = effect;
		return this;
	}
	
	public ActionConfiguration setDefaultTargetDamage(int base, DamageType type, AttackEffect effect) {
		this.defaultBaseTargetDamage = base;
		this.defaultTargetDamageType = type;
		this.defaultTargetAttackEffect = effect;
		return this;
	}
	
	public ActionConfiguration resetDefaultAttack() {
		resetDefaultSourceDamage();
		resetDefaultTargetDamage();
		return this;
	}
	
	public ActionConfiguration resetDefaultSourceDamage() {
		this.defaultBaseSourceDamage = 0;
		this.defaultSourceDamageType = null;
		this.defaultSourceAttackEffect = null;
		return this;
	}
	
	public ActionConfiguration resetDefaultTargetDamage() {
		this.defaultBaseTargetDamage = 0;
		this.defaultTargetDamageType = null;
		this.defaultTargetAttackEffect = null;
		return this;
	}
	
	public ActionConfiguration calculateAndDealDamageToSource() {
		return calculateAndDealDamageToTarget(defaultBaseSourceDamage, defaultSourceDamageType, defaultSourceAttackEffect);
	}
	
	public ActionConfiguration calculateAndDealDamageToSource(DamageType type, AttackEffect effect) {
		return calculateAndDealDamageToTarget(defaultBaseSourceDamage, type, effect);
	}
	
	public ActionConfiguration calculateAndDealDamageToSource(int baseDamage, DamageType type, AttackEffect effect) {
		return dealBaseDamageToSource(applySourceModifiers(baseDamage), type, effect);
	}
	
	public ActionConfiguration dealBaseDamageToSource(int damage) {
		return dealBaseDamageToSource(damage, defaultSourceDamageType, defaultSourceAttackEffect);
	}
	
	public ActionConfiguration dealBaseDamageToSource(int damage, DamageType type, AttackEffect effect) {
		return enqueueAction(new DamageAction(source, new DamageInfo(source, damage, type), effect));
	}
	
	public ActionConfiguration calculateAndDealDamageToTarget() {
		return calculateAndDealDamageToTarget(defaultBaseTargetDamage, defaultTargetDamageType, defaultTargetAttackEffect);
	}
	
	public ActionConfiguration calculateAndDealDamageToTarget(DamageType type, AttackEffect effect) {
		return calculateAndDealDamageToTarget(defaultBaseTargetDamage, type, effect);
	}
	
	public ActionConfiguration calculateAndDealDamageToTarget(int baseDamage, DamageType type, AttackEffect effect) {
		return dealBaseDamageToTarget(applyTargetModifiers(baseDamage), type, effect);
	}
	
	public ActionConfiguration dealBaseDamageToTarget(int damage) {
		return dealBaseDamageToTarget(damage, defaultTargetDamageType, defaultTargetAttackEffect);
	}
	
	public ActionConfiguration dealBaseDamageToTarget(int damage, DamageType type, AttackEffect effect) {
		return enqueueAction(new DamageAction(target, new DamageInfo(source, damage, type), effect));
	}
	
	public ActionConfiguration enqueueAction(AbstractGameAction action) {
		if (bottomActions == null) {
			bottomActions = new ArrayList<>();
		}
		bottomActions.add(action);
		return this;
	}
	
	public ActionConfiguration enqueueActionToTop(AbstractGameAction action) {
		if (topActions == null) {
			topActions = new ArrayList<>();
		}
		topActions.add(action);
		return this;
	}
	
	public ActionConfiguration enqueueActions(AbstractPlayer p, AbstractMonster m) {
		if (bottomActions != null) {
			for (AbstractGameAction action : bottomActions) {
				if (action.source == PLAYER_PLACEHOLDER) {
					action.target = p;
				}
				if (action.target == MONSTER_PLACEHOLDER) {
					action.target = m;
				}
				AbstractDungeon.actionManager.addToBottom(action);
			}
		}
		if (topActions != null) {
			for (AbstractGameAction action : topActions) {
				if (action.source == PLAYER_PLACEHOLDER) {
					action.target = p;
				}
				if (action.target == MONSTER_PLACEHOLDER) {
					action.target = m;
				}
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
