package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.PrestidigitationPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class PrestidigitationA extends AbstractDynamicCard {
    public static final String ID = JorbsMod.makeID(PrestidigitationA.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Commons/prestidigitation_a.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int EFFECT_MAGNITUDE = 1;
    private static final int UPGRADE_PLUS_EFFECT_MAGNITUDE = 1;

    public PrestidigitationA() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = EFFECT_MAGNITUDE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new PrestidigitationPower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT_MAGNITUDE);
            initializeDescription();
        }
    }
}
