package stsjorbsmod.cards.wanderer;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EntombedField;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class AnimateObjects extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(AnimateObjects.class.getSimpleName());
    public static final String IMG = makeCardPath("AoE_Uncommons/animate_objects.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE_PER_MATCHING_CARD = 3;
    private static final int UPGRADE_PLUS_PER_MATCHING_CARD = 2;

    public AnimateObjects() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE_PER_MATCHING_CARD;
        isMultiDamage = true;
    }

    protected int calculateDamageInstanceCount() {
        int numMatchingCards = 0;
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (EntombedField.entombed.get(c) || StSLib.getMasterDeckEquivalent(c) == null) {
                numMatchingCards++;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (EntombedField.entombed.get(c) || StSLib.getMasterDeckEquivalent(c) == null) {
                numMatchingCards++;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (EntombedField.entombed.get(c) || StSLib.getMasterDeckEquivalent(c) == null) {
                numMatchingCards++;
            }
        }
        return numMatchingCards;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        for (int i = 0; i < calculateDamageInstanceCount(); ++i) {
            addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.FIRE));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_PER_MATCHING_CARD);
        }
    }
}
