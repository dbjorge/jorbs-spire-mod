package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import stsjorbsmod.JorbsMod;

public class IfEnemyIntendsToAttackAction extends AbstractGameAction {
    private static final String UI_ID = JorbsMod.makeID(IfEnemyIntendsToAttackAction.class.getSimpleName());
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractGameAction actionToChain;
    private AbstractMonster specificEnemy;

    public IfEnemyIntendsToAttackAction(AbstractGameAction actionToChain) {
        this(null, actionToChain);
    }

    public IfEnemyIntendsToAttackAction(AbstractMonster specificEnemy, AbstractGameAction actionToChain) {
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
        this.actionToChain = actionToChain;
        this.specificEnemy = specificEnemy;
    }

    private static boolean intendsToAttack(AbstractMonster enemy) {
        return enemy.getIntentBaseDmg() >= 0;
    }
    public void update() {
        boolean intendsToAttack = this.specificEnemy != null ?
            intendsToAttack(specificEnemy) :
            AbstractDungeon.getMonsters().monsters.stream().anyMatch(IfEnemyIntendsToAttackAction::intendsToAttack);

        if (intendsToAttack) {
            AbstractDungeon.actionManager.addToBottom(actionToChain);
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }

        this.isDone = true;
    }
}
