package stsjorbsmod.cards.wanderer;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.AutoExhumeBehavior;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.AutoExhumeField;
import stsjorbsmod.powers.SnappedPower;

import java.util.EnumSet;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class LoseGrip extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(LoseGrip.class.getSimpleName());
    public static final String IMG = makeCardPath("Block_Uncommons/lose_grip.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int BLOCK = 10;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    public LoseGrip() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        block = baseBlock = BLOCK;
        exhaust = true;
        AutoExhumeField.autoExhumeBehavior.set(this, AutoExhumeBehavior.EXHUME_ON_SNAP);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeDescription();
        }
    }
}
