package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DiscoveryAtCostAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnCardExhumedSubscriber;
import stsjorbsmod.cards.OnEntombedSubscriber;
import stsjorbsmod.cards.wanderer.materialcomponents.MaterialComponentsDeck;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.patches.SelfExhumeFields;
import stsjorbsmod.powers.EntombedGrimoirePower;
import stsjorbsmod.powers.ForbiddenGrimoireDelayedExhumePower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class ForbiddenGrimoire extends CustomJorbsModCard implements OnCardExhumedSubscriber, OnEntombedSubscriber {
    public static final String ID = JorbsMod.makeID(ForbiddenGrimoire.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int CARD_PLAYS_TO_EXHUME = 4;

    public static final int EXHUME_TURN = 7;

    private String entombedGrimoirePowerInstanceID;

    public ForbiddenGrimoire() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARD_PLAYS_TO_EXHUME;
        EntombedField.entombed.set(this, true);
        SelfExhumeFields.selfExhumeOnSnap.set(this, true);
        EphemeralField.ephemeral.set(this, true);
        tags.add(LEGENDARY);

        upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscoveryAtCostAction(MaterialComponentsDeck.drawUniqueRandomCards(3), true));
        addToBot(new ApplyPowerAction(p, p, new ForbiddenGrimoireDelayedExhumePower(p, this, magicNumber)));
    }

    @Override
    public void onCardExhumed() {
        addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, entombedGrimoirePowerInstanceID));
        SelfExhumeFields.selfExhumeOnSnap.set(this, false);
    }

    @Override
    public void onCardEntombed() {
        AbstractPower entombedGrimoirePower = new EntombedGrimoirePower(AbstractDungeon.player, this, ForbiddenGrimoire.EXHUME_TURN);
        entombedGrimoirePowerInstanceID = entombedGrimoirePower.ID;
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, entombedGrimoirePower));
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard c = super.makeStatEquivalentCopy();
        ((ForbiddenGrimoire) c).entombedGrimoirePowerInstanceID = entombedGrimoirePowerInstanceID;
        return c;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
        }
    }
}
