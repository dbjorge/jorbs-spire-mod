package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class WeightOfMemory extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(WeightOfMemory.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Commons/weight_of_memory.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int DAMAGE = 12;
    private static final int DAMAGE_PER_REMEMBER_CARD = 2;
    private static final int UPGRADE_PLUS_DAMAGE_PER_REMEMBER_CARD = 2;

    public WeightOfMemory() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_PER_REMEMBER_CARD;
    }

    private static int countRememberMemoryCards() {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (isRememberMemoryCard(c)) { ++count; }
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (isRememberMemoryCard(c)) { ++count; }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (isRememberMemoryCard(c)) { ++count; }
        }
        return count;
    }

    private static boolean isRememberMemoryCard(AbstractCard c) {
        return c.hasTag(REMEMBER_MEMORY);
    }

    @Override
    protected int calculateBonusBaseDamage() {
        return this.magicNumber * countRememberMemoryCards();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_PER_REMEMBER_CARD);
            initializeDescription();
        }
    }
}
