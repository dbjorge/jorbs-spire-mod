package stsjorbsmod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.beyond.Darkling;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BackAttackPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import stsjorbsmod.actions.DecreaseMaxHpAction;
import stsjorbsmod.actions.GainSpecificClarityAction;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;

import java.util.ArrayList;

public class SacrificePower extends CustomJorbsModPower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(SacrificePower.class);
    public static final String POWER_ID = STATIC.ID;

    public SacrificePower(AbstractMonster owner) {
        super(STATIC);
        this.isTurnBased = false;
        this.owner = owner;
        this.updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        super.onUseCard(card, action);

        int hpCost = card.cost;
        card.setCostForTurn(0);

        addToBot(new DecreaseMaxHpAction(AbstractDungeon.player, AbstractDungeon.player, hpCost, AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public void updateDescription() {
        this.description = String.format((this.amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1]), this.amount);
    }

    @Override
    public AbstractPower makeCopy() {
        return new SacrificePower((AbstractMonster) owner);
    }
}
