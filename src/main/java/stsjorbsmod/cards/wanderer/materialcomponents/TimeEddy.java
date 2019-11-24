package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.AdvancePowersThroughTimeAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.util.ReflectionUtils;

public class TimeEddy extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(TimeEddy.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int POWER_COUNTER_INCREMENT = 1;
    private static final int UPGRADE_PLUS_POWER_COUNTER_INCREMENT = 1;

    public TimeEddy() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = POWER_COUNTER_INCREMENT;
        EphemeralField.ephemeral.set(this, true);
    }

    private static boolean shouldAffectPower(AbstractPower power) {
        return power.type == PowerType.BUFF || power.type == PowerType.DEBUFF;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AdvancePowersThroughTimeAction(p, magicNumber, TimeEddy::shouldAffectPower));
    }

    @Override
    public boolean shouldGlowGold() {
        return AbstractDungeon.player.powers.stream().anyMatch(p ->
                (shouldAffectPower(p) && (Boolean)ReflectionUtils.getPrivateField(p, AbstractPower.class, "isTurnBased")));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_POWER_COUNTER_INCREMENT);
            upgradeDescription();
        }
    }
}
