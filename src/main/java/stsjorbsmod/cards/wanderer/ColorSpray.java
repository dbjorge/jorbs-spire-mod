package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.GainClarityOfCurrentMemoryAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.EnvyMemory;
import stsjorbsmod.powers.BanishedPower;
import stsjorbsmod.powers.BurningPower;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class ColorSpray extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ColorSpray.class.getSimpleName());
    public static final String IMG = makeCardPath("AoE_Uncommons/color_spray.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = 3;

    public ColorSpray() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        isMultiDamage = true;

        this.tags.add(REMEMBER_MEMORY);
    }

    private AbstractPower generateRandomDebuff(AbstractPlayer p, AbstractMonster m) {
        switch(AbstractDungeon.cardRandomRng.random(0, 4)) {
            case 0: return new WeakPower(m, 2, false);
            case 1: return new VulnerablePower(m, 2, false);
            case 2: return new BurningPower(m, p, 4);
            case 3: return new StrengthPower(m, -2);
            case 4: return new BanishedPower(m, p, 1);

            default: throw new RuntimeException("random debuff type out of range");
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.FIRE));

        for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            final AbstractPower randomDebuff = generateRandomDebuff(p, m);
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, randomDebuff, randomDebuff.amount, true, AttackEffect.NONE));
        }

        addToBot(new RememberSpecificMemoryAction(p, EnvyMemory.STATIC.ID));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeDescription();
        }
    }
}
