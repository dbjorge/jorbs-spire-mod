package stsjorbsmod.cards.wanderer;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.patches.EphemeralField;

public class AnimateObjects extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(AnimateObjects.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int UPGRADED_COST = 1;
    private static final int THORNS_PER_MATCHING_CARD = 1;

    public AnimateObjects() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = 0;
        baseMetaMagicNumber = metaMagicNumber = THORNS_PER_MATCHING_CARD;
        EphemeralField.ephemeral.set(this, true);
    }

    private int countCardsThatDidNotStartCombatInDeck(CardGroup group) {
        return (int) group.group.stream()
                .filter(c -> EntombedField.entombed.get(c) || StSLib.getMasterDeckEquivalent(c) == null)
                .count();
    }

    @Override
    protected int calculateBonusMagicNumber() {
        return countCardsThatDidNotStartCombatInDeck(AbstractDungeon.player.discardPile) +
                countCardsThatDidNotStartCombatInDeck(AbstractDungeon.player.drawPile) +
                countCardsThatDidNotStartCombatInDeck(AbstractDungeon.player.hand);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        if (magicNumber > 0) {
            addToBot(new ApplyPowerAction(p, p, new ThornsPower(p, magicNumber)));
        }
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return cardStrings.EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            upgradeDescription();
        }
    }
}