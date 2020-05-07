package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DecreaseMaxHpAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

public class ProdigalMemory extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ProdigalMemory.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int DRAW = 3;
    private static final int MAX_HP_LOSS = 2;
    private static final int UPGRADE_MAX_HP_LOSS = -1;

    public ProdigalMemory() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAX_HP_LOSS;
        urMagicNumber = baseUrMagicNumber = DRAW;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        if (this.dontTriggerOnUseCard) {
            SelfExertField.selfExert.set(this, true);
            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
        }
        else {
            addToBot(new DrawCardAction(DRAW));
            addToBot(new DecreaseMaxHpAction(p, p, magicNumber, AbstractGameAction.AttackEffect.POISON));
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAX_HP_LOSS);
            upgradeDescription();
        }
    }
}
