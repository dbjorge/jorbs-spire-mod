package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.LustMemory;
import stsjorbsmod.powers.BurningPower;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

/**
 * Base: Deal 21 damage to all enemies
 * Remember Lust
 * Upgraded: Apply 3 Burning to all enemies
 */
public class Fireball extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Fireball.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Rares/fireball.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int BURNING = 0;
    private static final int UPGRADE_PLUS_BURNING = 4;

    public Fireball() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BURNING;
        isMultiDamage = true;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, LustMemory.STATIC.ID));
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.FIRE));
        if(magicNumber > 0) {
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                addToBot(new ApplyPowerAction(mo, p, new BurningPower(mo, p, this.magicNumber)));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_BURNING);
            upgradeDescription();
        }
    }
}
