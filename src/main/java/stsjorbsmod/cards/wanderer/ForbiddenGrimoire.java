package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.EntombedBehavior;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.powers.ForbiddenGrimoireDelayedExhumePower;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.patches.MaterialComponentsDeckPatch.MATERIAL_COMPONENT;

public class ForbiddenGrimoire extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ForbiddenGrimoire.class.getSimpleName());
    public static final String IMG = makeCardPath("Curses/forbidden_grimoire.png");

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 0;
    private static final int CARD_PLAYS_TO_EXHUME = 2;

    public ForbiddenGrimoire() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = CARD_PLAYS_TO_EXHUME;
        EntombedField.entombedBehavior.set(this, EntombedBehavior.RECOVER_ON_START_OF_TURN_7);
        exhaust = true;

        upgrade(); // Always starts upgraded
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DiscoveryAction(MATERIAL_COMPONENT));
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
