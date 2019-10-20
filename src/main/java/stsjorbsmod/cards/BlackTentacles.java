package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.BlackTentaclesOverrideAttackDamageRandomEnemyActionPatch;
import stsjorbsmod.powers.BlackTentaclesPower;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class BlackTentacles extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(BlackTentacles.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Commons/black_tentacles.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int EFFECT_TURN_DURATION = 1;
    private static final int UPGRADE_PLUS_EFFECT_TURN_DURATION = 1;

    public BlackTentacles() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = EFFECT_TURN_DURATION;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        enqueueAction(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SLASH_HORIZONTAL));
        enqueueAction(new ApplyPowerAction(m, p, new BlackTentaclesPower(m, p, this.magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_EFFECT_TURN_DURATION);
            upgradeDescription();
        }
    }
}
