package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DamageWithOnKillEffectAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.LastOverkillDamageField;
import stsjorbsmod.powers.ReapAndSowPower;

public class ReapAndSow extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ReapAndSow.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 25;
    private static final int UPGRADE_DAMAGE = 8;

    public ReapAndSow() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Runnable onKillEffect = () -> addToBot(new ApplyPowerAction(p, p, new ReapAndSowPower(p, LastOverkillDamageField.lastOverkillDamage.get(m))));

        addToBot(new DamageWithOnKillEffectAction(m, new DamageInfo(p, damage, damageTypeForTurn), onKillEffect, false));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }
}
