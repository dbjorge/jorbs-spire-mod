package stsjorbsmod.memories;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class EnvyMemory extends AbstractMemory {
    public static final StaticMemoryInfo STATIC = StaticMemoryInfo.Load(EnvyMemory.class);

    private static final int VULNERABLE_ON_REMEMBER = 1;
    private static final int VULNERABLE_ON_TARGET_ENEMY = 1;

    public EnvyMemory(final AbstractCreature owner) {
        super(STATIC, MemoryType.SIN, owner);
        setDescriptionPlaceholder("!M!", VULNERABLE_ON_TARGET_ENEMY);
    }

    @Override
    public void onRemember() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new VulnerablePower(owner, VULNERABLE_ON_REMEMBER, false), VULNERABLE_ON_REMEMBER));
    }

    private void applyPassiveVulnerable(AbstractMonster monster) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(monster, owner, new VulnerablePower(monster, VULNERABLE_ON_TARGET_ENEMY, false), VULNERABLE_ON_TARGET_ENEMY));
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster monster) {
        if (!isPassiveEffectActive()) {
            return;
        }

        boolean isTargetingSingleEnemy = monster != null && card.target == CardTarget.ENEMY || card.target == CardTarget.SELF_AND_ENEMY;
        boolean isTargetingAllEnemies = card.target == CardTarget.ALL || card.target == CardTarget.ALL_ENEMY;
        if (isTargetingSingleEnemy) {
            this.flash();
            applyPassiveVulnerable(monster);
        } else if(isTargetingAllEnemies) {
            this.flash();
            AbstractDungeon.getMonsters().monsters.forEach(this::applyPassiveVulnerable);
        }
    }
}
