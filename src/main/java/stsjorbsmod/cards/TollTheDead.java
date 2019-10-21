package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.util.MonsterDamageTracker;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class TollTheDead extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(TollTheDead.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Uncommons/toll_the_dead.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;

    public TollTheDead() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SMASH));

        if (MonsterDamageTracker.hasMonsterBeenDamagedThisTurn(m)) {
            enqueueAction(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SMASH));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
