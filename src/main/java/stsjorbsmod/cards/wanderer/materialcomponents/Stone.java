package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;

public class Stone extends MaterialComponent {
    public static final String ID = JorbsMod.makeID(Stone.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int SLOW_MULTIPLIER = 2;
    private static final int UPGRADE_PLUS_SLOW_MULTIPLIER = 1;

    public Stone() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        bannerImageRarity = CardRarity.RARE;
        magicNumber = baseMagicNumber = SLOW_MULTIPLIER;
        selfRetain = true;
        exhaust = true;
    }

    @Override
    public void useMaterialComponent(AbstractPlayer p, AbstractMonster m) {
        AbstractPower possibleExistingSlowPower = m.getPower(SlowPower.POWER_ID);
        if (possibleExistingSlowPower == null) {
            addToBot(new ApplyPowerAction(m, p, new SlowPower(m, 0), 0));
        } else {
            int stacksToAdd = possibleExistingSlowPower.amount * (magicNumber - 1);
            addToBot(new ApplyPowerAction(m, p, new SlowPower(m, stacksToAdd), stacksToAdd));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_SLOW_MULTIPLIER);
            upgradeDescription();
        }
    }
}
