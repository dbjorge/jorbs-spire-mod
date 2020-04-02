package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

public class Rebuttal extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Rebuttal.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int THORNS_RETAINED = 2;
    private static final int THORNS_EXERTED = 4;
    private static final int UPGRADE_PLUS_THORNS_RETAINED = 1;
    private static final int UPGRADE_PLUS_THORNS_EXERTED = 1;

    private boolean thornsApplied = false;


    public Rebuttal() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        selfRetain = true;
        baseMagicNumber = THORNS_EXERTED;
        urMagicNumber = baseUrMagicNumber = THORNS_RETAINED;
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void onRetained() {
        if (!thornsApplied) {
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, urMagicNumber)));
            thornsApplied = true;
        }
    }

    private void removeRetainThorns() {
        if (thornsApplied) {
            AbstractPlayer p = AbstractDungeon.player;
            addToBot(new ReducePowerAction(p, p, ThornsPower.POWER_ID, urMagicNumber));
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
        upgradeMagicNumber(UPGRADE_PLUS_THORNS_EXERTED);
        upgradeUrMagicNumber(UPGRADE_PLUS_THORNS_RETAINED);
    }
}
