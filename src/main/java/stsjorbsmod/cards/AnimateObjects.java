package stsjorbsmod.cards;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.EnvyMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class AnimateObjects extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(AnimateObjects.class.getSimpleName());
    public static final String IMG = makeCardPath("AoE_Uncommons/animate_objects.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

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
