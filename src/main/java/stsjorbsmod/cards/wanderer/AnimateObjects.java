package stsjorbsmod.cards.wanderer;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EntombedField;

import java.util.Optional;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class AnimateObjects extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(AnimateObjects.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE_PER_MATCHING_CARD = 3;
    private static final int UPGRADE_PLUS_PER_MATCHING_CARD = 2;

    public AnimateObjects() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE_PER_MATCHING_CARD;
        baseMagicNumber = 0;
        isMultiDamage = true;
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
        for (int i = 0; i < magicNumber; ++i) {
            addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.FIRE));
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
            upgradeDamage(UPGRADE_PLUS_PER_MATCHING_CARD);
            upgradeDescription();
        }
    }
}