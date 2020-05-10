package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

public class Clarify extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Clarify.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int CARDS_RETAINED = 1;
    private static final int UPGRADE_PLUS_CARDS_RETAINED = 1;

    private boolean thornsApplied = false;


    public Clarify() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        selfRetain = true;
        baseMagicNumber = magicNumber = CARDS_RETAINED;
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void onRetained() {
        if (!thornsApplied) {
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new ApplyPowerAction(p, p, new RetainCardPower(p, this.magicNumber), this.magicNumber));
            thornsApplied = true;
        }
    }

    private void removeRetainThorns() {
        if (thornsApplied) {
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new ReducePowerAction(p, p, RetainCardPower.POWER_ID, magicNumber));
            thornsApplied = false;
        }
    }

    @Override
    public void triggerOnExhaust() {
        removeRetainThorns();
    }

    @Override
    public void triggerOnManualDiscard() {
        removeRetainThorns();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        removeRetainThorns();
        addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        upgradeName();
        upgradeDescription();
        upgradeMagicNumber(UPGRADE_PLUS_CARDS_RETAINED);
    }
}
