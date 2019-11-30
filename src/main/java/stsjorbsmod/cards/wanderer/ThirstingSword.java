package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ThirstingSwordBurningVampireAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.BurningPower;

public class ThirstingSword extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ThirstingSword.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int BURNING = 3;
    private static final int LOSE_MAX_HP = 3;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int UPGRADE_BURNING = 3;
    private static final int UPGRADE_LOSE_MAX_HP = -1;

    public ThirstingSword() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BURNING;
        metaMagicNumber = baseMetaMagicNumber = LOSE_MAX_HP;

        exhaust = true;

        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new BurningPower(m, p, magicNumber)));
        addToBot(new ThirstingSwordBurningVampireAction(m, p, metaMagicNumber));
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_BURNING);
            upgradeMetaMagicNumber(UPGRADE_LOSE_MAX_HP);
            upgradeDescription();
        }
    }

}
