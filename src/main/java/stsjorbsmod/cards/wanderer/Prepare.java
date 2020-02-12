package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.CoilPower;
import stsjorbsmod.util.PowerUtils;

public class Prepare extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Prepare.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int COIL = 5;
    private static final int UPGRADE_PLUS_COIL = 2;

    public Prepare() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        metaMagicNumber = baseMetaMagicNumber = COIL;
        magicNumber = baseMagicNumber = 0; // Represents current amount of Coil in card description
        baseBlock = 0;
    }

    // Note: because this is dependent on Coil and Coil amount changes during use(), this won't be
    // accurate if used as part of the card description. So don't.
    @Override
    protected int calculateBonusBaseBlock() {
        return PowerUtils.getPowerAmount(AbstractDungeon.player, CoilPower.POWER_ID);
    }

    // This is used for the card description to display current amount of coil, unmodified by Frail/Dexterity/etc
    @Override
    protected int calculateBonusMagicNumber() {
        return PowerUtils.getPowerAmount(AbstractDungeon.player, CoilPower.POWER_ID);
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        addToBot(new ApplyPowerAction(p, p, new CoilPower(p, metaMagicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_COIL);
            upgradeDescription();
        }
    }
}
