package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DiscoveryAtCostAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnCardExhumedSubscriber;
import stsjorbsmod.cards.wanderer.materialcomponents.MaterialComponentsDeck;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.patches.SelfExhumeFields;
import stsjorbsmod.powers.ForbiddenGrimoireDelayedExhumePower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class ForbiddenGrimoire extends CustomJorbsModCard implements OnCardExhumedSubscriber {
    public static final String ID = JorbsMod.makeID(ForbiddenGrimoire.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int CARD_PLAYS_TO_EXHUME = 3;

    public static final int EXHUME_TURN = 7;

    public ForbiddenGrimoire() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARD_PLAYS_TO_EXHUME;
        EntombedField.entombed.set(this, true);
        SelfExhumeFields.selfExhumeAtStartOfTurn7.set(this, true);
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
        SelfExhumeFields.selfExhumeAtStartOfTurn7.set(this, false);
        SelfExhumeFields.selfExhumeOnSnap.set(this, false);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
        }
    }
}
