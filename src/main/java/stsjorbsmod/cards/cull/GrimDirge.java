package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnEntombedSubscriber;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.powers.EntombedGrimDirgePower;

public class GrimDirge extends CustomJorbsModCard implements OnEntombedSubscriber {
    public static final String ID = JorbsMod.makeID(GrimDirge.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 30;
    private static final int UPGRADE_DAMAGE = 10;
    private static final int EXHUME_TURN = 3;

    private String grimDirgePowerInstanceID;



    public GrimDirge() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        EntombedField.entombed.set(this, true);
        EphemeralField.ephemeral.set(this, true);
        baseDamage = DAMAGE;
        baseMagicNumber = EXHUME_TURN;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
    }


    @Override
    public void onCardEntombed() {
        AbstractPower grimDirgePower = new EntombedGrimDirgePower(AbstractDungeon.player, this, EXHUME_TURN);
        grimDirgePowerInstanceID = grimDirgePower.ID;
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, grimDirgePower));
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard c = super.makeStatEquivalentCopy();
        ((GrimDirge) c).grimDirgePowerInstanceID = grimDirgePowerInstanceID;
        return c;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeName();
            upgradeDescription();
        }
    }
}
