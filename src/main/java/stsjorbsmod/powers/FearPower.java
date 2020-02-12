package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import stsjorbsmod.actions.GainSpecificClarityAction;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;

import java.util.ArrayList;

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
                ArrayList<Float> monsterDisplacementsFromPlayer = new ArrayList<>();
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
                        // negative if monster to the left of player, positive if monster to the right of player
                        monsterDisplacementsFromPlayer.add(m.drawX - AbstractDungeon.player.drawX);
                    }
                }
                // flipHorizontal = false -> face right
                // flipHorizontal = true  -> face left
                if (monsterDisplacementsFromPlayer.stream().noneMatch(x -> x < 0 == AbstractDungeon.player.flipHorizontal)) {
                    // If none of the remaining monsters are on the same side the player is facing, then turn
                    AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
                    addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SurroundedPower.POWER_ID));
                    for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                        addToBot(new RemoveSpecificPowerAction(m, m, BackAttackPower.POWER_ID));
                    }
                }

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
