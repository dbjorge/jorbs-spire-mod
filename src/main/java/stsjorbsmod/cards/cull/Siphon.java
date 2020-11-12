package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DamageWithOnKillEffectAction;
import stsjorbsmod.actions.IncreaseMaxHpByFlatAmountAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Siphon extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Siphon.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 9;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int HEAL_ON_FATAL = 4;
    private static final int UPGRADE_PLUS_HEAL_ON_FATAL = 1;
    private static final int MAX_HP_ON_FATAL = 1;

    public Siphon() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        metaMagicNumber = baseMetaMagicNumber = MAX_HP_ON_FATAL;
        magicNumber = baseMagicNumber = HEAL_ON_FATAL;
        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Runnable onKillEffect = () -> {
            addToBot(new HealAction(p, p, magicNumber));
            addToBot(new IncreaseMaxHpByFlatAmountAction(p, p, metaMagicNumber, true));
        };
        this.addToBot(new DamageWithOnKillEffectAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                onKillEffect, true));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_HEAL_ON_FATAL);

            upgradeDescription();
        }
    }
}
