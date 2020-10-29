package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.RunicPyramid;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DecreaseMaxHpAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.ExertedField;
import stsjorbsmod.patches.SelfExertField;
import stsjorbsmod.powers.RepressedMemoryPower;

import java.util.Arrays;

public class RepressedMemory extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(RepressedMemory.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int MAX_HP_LOSS = 1;
    private static final int ENERGY_GAIN = 2;
    private static final int CARD_DRAW = 3;
    private static final int UPGRADE_MAX_HP_LOSS_AMOUNT = -1;

    public RepressedMemory() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARD_DRAW;
        urMagicNumber = baseUrMagicNumber = ENERGY_GAIN;
        metaMagicNumber = baseMetaMagicNumber = MAX_HP_LOSS;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_MAX_HP_LOSS_AMOUNT);
            upgradeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new DrawCardAction(this.magicNumber));
        addToBot(new GainEnergyAction(this.urMagicNumber));
        addToBot(new ApplyPowerAction(abstractPlayer, abstractPlayer, new RepressedMemoryPower(abstractPlayer, this)));
    }

    @Override
    public void triggerOnManualDiscard() {
        if (!upgraded)
            this.addToBot(new DecreaseMaxHpAction(AbstractDungeon.player, AbstractDungeon.player, this.metaMagicNumber, AbstractGameAction.AttackEffect.POISON));
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        if (AbstractDungeon.player.hand.group.contains(this)) {
            if (!AbstractDungeon.player.hasRelic(RunicPyramid.ID) && !upgraded) {
                addToBot(new DecreaseMaxHpAction(AbstractDungeon.player, AbstractDungeon.player, this.metaMagicNumber, AbstractGameAction.AttackEffect.POISON));
            }
        }
    }
}
