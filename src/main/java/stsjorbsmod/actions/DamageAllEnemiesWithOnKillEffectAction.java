package stsjorbsmod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DamageAllEnemiesWithOnKillEffectAction extends AbstractGameAction {
    public int[] damage;
    private int baseDamage;
    private boolean firstFrame;
    private boolean utilizeBaseDamage;
    private Runnable onKillCallback;
    private boolean worksOnMinions;


    public DamageAllEnemiesWithOnKillEffectAction(AbstractCreature source, int[] amount, DamageType type, AttackEffect effect, Runnable onKillCallback, boolean worksOnMinions) {
        this.firstFrame = true;
        this.utilizeBaseDamage = false;
        this.source = source;
        this.damage = amount;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        this.onKillCallback = onKillCallback;
        this.worksOnMinions = worksOnMinions;
        this.duration = Settings.ACTION_DUR_FAST;

    }

    public DamageAllEnemiesWithOnKillEffectAction(AbstractCreature source, int[] amount, DamageType type, AttackEffect effect, Runnable onKillCallback) {
        this(source, amount, type, effect, onKillCallback, false);
    }

    public DamageAllEnemiesWithOnKillEffectAction(AbstractPlayer player, int baseDamage, DamageType type, AttackEffect effect, Runnable onKillCallback, boolean worksOnMinions) {
        this(player, null, type, effect, onKillCallback, worksOnMinions);
        this.baseDamage = baseDamage;
        this.utilizeBaseDamage = true;
        this.worksOnMinions = worksOnMinions;
    }

    public DamageAllEnemiesWithOnKillEffectAction(AbstractPlayer player, int baseDamage, DamageType type, AttackEffect effect, Runnable onKillCallback) {
        this(player, null, type, effect, onKillCallback, false );
        this.baseDamage = baseDamage;
        this.utilizeBaseDamage = true;
    }

    public void update() {
        int nbMonsters;
        nbMonsters = AbstractDungeon.getCurrRoom().monsters.monsters.size();
        if (this.firstFrame) {
            boolean playedMusic = false;
            if (this.utilizeBaseDamage) {
                this.damage = DamageInfo.createDamageMatrix(this.baseDamage);
            }

            for(int i = 0; i < nbMonsters; ++i) {
                AbstractMonster currentMonster = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);

                if (!currentMonster.isDying && currentMonster.currentHealth > 0 && !currentMonster.isEscaping) {
                    if (playedMusic) {
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(currentMonster.hb.cX, currentMonster.hb.cY, this.attackEffect, true));
                    } else {
                        playedMusic = true;
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(currentMonster.hb.cX, currentMonster.hb.cY, this.attackEffect));
                    }
                }
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {

            for (AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(this.damage);
            }

            for(int i = 0; i < nbMonsters; ++i) {
                AbstractMonster currentMonster = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if (!currentMonster.isDeadOrEscaped()) {
                    if (this.attackEffect == AttackEffect.POISON) {
                        currentMonster.tint.color.set(Color.CHARTREUSE);
                        currentMonster.tint.changeColor(Color.WHITE.cpy());
                    } else if (this.attackEffect == AttackEffect.FIRE) {
                        currentMonster.tint.color.set(Color.RED);
                        currentMonster.tint.changeColor(Color.WHITE.cpy());
                    }

                    currentMonster.damage(new DamageInfo(this.source, this.damage[i], this.damageType));
                    if ((currentMonster.isDying || currentMonster.currentHealth <= 0) && !currentMonster.halfDead) {
                        if (worksOnMinions || !currentMonster.hasPower(MinionPower.POWER_ID)) {
                            this.onKillCallback.run();
                        }
                    }
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
            }
        }

    }
}
