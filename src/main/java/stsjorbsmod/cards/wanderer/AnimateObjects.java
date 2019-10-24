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

import static stsjorbsmod.JorbsMod.makeCardPath;

public class AnimateObjects extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(AnimateObjects.class.getSimpleName());
    public static final String IMG = makeCardPath("AoE_Uncommons/animate_objects.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE_PER_GENERATED_CARD = 3;
    private static final int UPGRADE_PLUS_DMG = 1;

    public AnimateObjects() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = 0;
        magicNumber = baseMagicNumber = DAMAGE_PER_GENERATED_CARD;
        isMultiDamage = true;
    }

    @Override
    protected int calculateBonusBaseDamage() {
        int numGeneratedCards = 0;
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (StSLib.getMasterDeckEquivalent(c) == null) {
                numGeneratedCards++;
            }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (StSLib.getMasterDeckEquivalent(c) == null) {
                numGeneratedCards++;
            }
        }

        return magicNumber * numGeneratedCards;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        enqueueAction(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.FIRE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DMG); // not upgradeDamage
            initializeDescription();
        }
    }
}
