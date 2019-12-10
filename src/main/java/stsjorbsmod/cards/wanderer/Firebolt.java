package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.BurningPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

/**
 * BASE: Deal 8 damage + 2 damage for each clarity
 * UPGRADE: Deal 8 damage. Apply 2 Burning for each clarity
 */
public class Firebolt extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Firebolt.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int AMOUNT_PER_CLARITY = 1;
    private static final int UPGRADE_PLUS_PER_CLARITY = 1;

    public Firebolt() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = 0;
        metaMagicNumber = baseMetaMagicNumber = AMOUNT_PER_CLARITY;
    }

    @Override
    public int calculateBonusMagicNumber() {
        return baseMetaMagicNumber * MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.FIRE));
        if (magicNumber > 0) {
            addToBot(new ApplyPowerAction(m, p, new BurningPower(m, p, magicNumber)));
        }
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_PER_CLARITY);
            upgradeDescription();
        }
    }
}
