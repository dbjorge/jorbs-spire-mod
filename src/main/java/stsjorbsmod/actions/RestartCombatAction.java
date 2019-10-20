package stsjorbsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EnableEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAndEnableControlsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;

import java.util.ArrayList;
import java.util.function.Predicate;

public class RestartCombatAction extends AbstractGameAction {
    private final boolean shouldRetainClarities;

    public RestartCombatAction(boolean shouldRetainClarities) {
        this.shouldRetainClarities = shouldRetainClarities;
    }

    @Override
    public void update() {
        MemoryManager memoryManager = MemoryManager.forPlayer(AbstractDungeon.player);
        ArrayList<AbstractMemory> retainedClarities = new ArrayList<>();

        if (shouldRetainClarities) {
            retainedClarities = memoryManager.currentClarities();
        }

        restartCombat();

        for (AbstractMemory clarity : retainedClarities) {
            AbstractDungeon.actionManager.addToBottom(new RememberSpecificMemoryAction(clarity));
        }

        isDone = true;
    }

    private void restartCombat() {
        // Heavily based on the PurpleSoulGem implementation in https://github.com/ReinaSHSL/Halation
        System.out.println("Remaking combat vs " + AbstractDungeon.lastCombatMetricKey);
        AbstractRoom room = AbstractDungeon.getCurrRoom();

        AbstractDungeon.fadeIn();
        AbstractDungeon.player.resetControllerValues();
        AbstractDungeon.effectList.clear();
        AbstractDungeon.topLevelEffects.clear();
        AbstractDungeon.topLevelEffectsQueue.clear();
        AbstractDungeon.effectsQueue.clear();
        AbstractDungeon.dungeonMapScreen.dismissable = true;
        AbstractDungeon.dungeonMapScreen.map.legend.isLegendHighlighted = false;

        AbstractDungeon.player.orbs.clear();
        AbstractDungeon.player.animX = 0.0f;
        AbstractDungeon.player.animY = 0.0f;
        AbstractDungeon.player.hideHealthBar();
        AbstractDungeon.player.hand.clear();
        AbstractDungeon.player.powers.clear();
        MemoryManager.forPlayer(AbstractDungeon.player).clear();
        AbstractDungeon.player.drawPile.clear();
        AbstractDungeon.player.discardPile.clear();
        AbstractDungeon.player.exhaustPile.clear();
        AbstractDungeon.player.limbo.clear();
        AbstractDungeon.player.loseBlock(true);
        AbstractDungeon.player.damagedThisCombat = 0;
        GameActionManager.turn = 1;

        AbstractDungeon.actionManager.monsterQueue.clear();
        AbstractDungeon.actionManager.monsterAttacksQueued = true;


        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onEnterRoom(room);
        }

        AbstractDungeon.actionManager.clear();

        room.monsters = MonsterHelper.getEncounter(AbstractDungeon.lastCombatMetricKey);
        room.monsters.init();

        // Prepare monsters
        for (AbstractMonster m : room.monsters.monsters) {
            m.showHealthBar();
            m.usePreBattleAction();
            m.useUniversalPreBattleAction();
        }

        AbstractDungeon.player.preBattlePrep();

        // From AbstractRoom.update(). Most of what happens at start of combat
        AbstractDungeon.actionManager.turnHasEnded = true;
        AbstractDungeon.topLevelEffects.add(new BattleStartEffect(false));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAndEnableControlsAction(AbstractDungeon.player.energy.energyMaster));

        AbstractDungeon.player.applyStartOfCombatPreDrawLogic();
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.gameHandSize));

        AbstractDungeon.actionManager.addToBottom(new EnableEndTurnButtonAction());
        AbstractDungeon.overlayMenu.showCombatPanels();
        AbstractDungeon.player.applyStartOfCombatLogic();
        AbstractDungeon.player.applyStartOfTurnRelics();
        AbstractDungeon.player.applyStartOfTurnPostDrawRelics();
        AbstractDungeon.player.applyStartOfTurnCards();
        AbstractDungeon.player.applyStartOfTurnPowers();
        AbstractDungeon.player.applyStartOfTurnOrbs();
    }
}
