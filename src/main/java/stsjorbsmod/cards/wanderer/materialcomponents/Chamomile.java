package stsjorbsmod.cards.wanderer.materialcomponents;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Chamomile extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Chamomile.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;

    public Chamomile() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        bannerImageRarity = CardRarity.UNCOMMON;
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RemoveSpecificPowerAction(p, p, FrailPower.POWER_ID));
        addToBot(new RemoveSpecificPowerAction(p, p, VulnerablePower.POWER_ID));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            EphemeralField.ephemeral.set(this, false);
            AlwaysRetainField.alwaysRetain.set(this, true);
            exhaust = true;
            upgradeDescription();
        }
    }
}
