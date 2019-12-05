package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DiscoveryAtCostAction;
import stsjorbsmod.cards.AutoExhumeBehavior;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.patches.SelfExhumeFields;
import stsjorbsmod.powers.ForbiddenGrimoireDelayedExhumePower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;
import static stsjorbsmod.patches.MaterialComponentsDeckPatch.MATERIAL_COMPONENT;

public class ForbiddenGrimoire extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ForbiddenGrimoire.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int CARD_PLAYS_TO_EXHUME = 3;

    public ForbiddenGrimoire() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARD_PLAYS_TO_EXHUME;
        EntombedField.entombed.set(this, true);
        SelfExhumeFields.selfExhumeAtStartOfTurn7.set(this, true);
        EphemeralField.ephemeral.set(this, true);
        tags.add(LEGENDARY);

        upgrade();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscoveryAtCostAction(MATERIAL_COMPONENT));
        if (!MemoryManager.forPlayer(p).isSnapped()) {
            addToBot(new ApplyPowerAction(p, p, new ForbiddenGrimoireDelayedExhumePower(p, this, magicNumber)));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
        }
    }
}
