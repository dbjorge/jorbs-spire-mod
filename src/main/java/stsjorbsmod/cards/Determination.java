package stsjorbsmod.cards;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.actions.RestartCombatAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.PrideMemory;
import stsjorbsmod.powers.FragilePower;

import java.util.function.Predicate;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Determination extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Determination.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Rares/determination.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int TURNS_UNTIL_SNAP = 1;
    private static final int UPGRADE_PLUS_TURNS_UNTIL_SNAP = 1;

    public Determination() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = TURNS_UNTIL_SNAP;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new RememberSpecificMemoryAction(new PrideMemory(p, false)));
        enqueueAction(new ApplyPowerAction(p, p, new FragilePower(p, magicNumber), magicNumber));
    }

    private void removeFromMasterDeck() {
        final AbstractCard masterDeckCard = StSLib.getMasterDeckEquivalent(this);
        if (masterDeckCard != null) {
            AbstractDungeon.player.masterDeck.removeCard(masterDeckCard);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_TURNS_UNTIL_SNAP);
            upgradeDescription();
        }
    }
}
