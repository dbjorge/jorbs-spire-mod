package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.actions.GainSpecificClarityAction;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;

public class FearPower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(FearPower.class);
    public static final String POWER_ID = STATIC.ID;

    public FearPower(AbstractMonster owner, int turnsBeforeFleeing) {
        super(STATIC);

        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;

        this.owner = owner;
        this.amount = turnsBeforeFleeing;

        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() { // note: monster's turn, not player's
        AbstractMonster monsterOwner = (AbstractMonster)owner;
        if (!monsterOwner.isDeadOrEscaped()) {
            --this.amount;
            updateDescription();

            if (this.amount == 0) {
                int remainingMonsters = 0;
                boolean flipHorizontal = true;
                for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    // We normally consider halfDead things to be remaining, assuming they'll have some condition by
                    // which they'll come back.
                    //
                    // Darklings are a special case where we consider them to be not-remaining when halfDead because
                    // DarklingEscapePatch will be specifically ending the fight if there are only halfDead ones left.
                    boolean isFunctionallyDead = m.isDying || m.isDead || m.isEscaping || (m.halfDead && m.id.equals(Darkling.ID));

                    // Checking intent is to cover for the "multiple FearPowers triggering on the same turn" case
                    if (!isFunctionallyDead && m.intent != Intent.ESCAPE) {
                        ++remainingMonsters;
                        // Turn if none of the remaining monsters are on the right of the player.
                        if (m.drawX > AbstractDungeon.player.drawX) {
                            flipHorizontal = false;
                        }
                    }
                }
                AbstractDungeon.player.flipHorizontal = flipHorizontal;

                AbstractDungeon.actionManager.addToBottom(new EscapeAction(monsterOwner));

                if (remainingMonsters == 0 && !AbstractDungeon.getCurrRoom().smoked) {
                    AbstractDungeon.getCurrRoom().smoked = true; // skips rewards
                    for (AbstractMemory clarity : MemoryManager.forPlayer(AbstractDungeon.player).currentClarities()) {
                        AbstractDungeon.actionManager.addToNextCombat(new GainSpecificClarityAction(AbstractDungeon.player, clarity.ID));
                    }
                }
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) { // note: monster's turn, not player's
        AbstractMonster monsterOwner = (AbstractMonster)owner;
        if (this.amount == 1) {
            monsterOwner.nextMove = (byte)-1;
            monsterOwner.setMove(monsterOwner.nextMove, Intent.ESCAPE);
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format((this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1]), this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new FearPower((AbstractMonster) owner, amount);
    }
}
