package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class WastingStrike extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(WastingStrike.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE = 10;
    private static final int DAMAGE_PER_EXHAUSTED = 2;
    private static final int UPGRADE_DAMAGE_PER_EXHAUSTED = 1;

    public WastingStrike() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = DAMAGE_PER_EXHAUSTED;
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public int calculateBonusBaseDamage() {
        return AbstractDungeon.player.exhaustPile.size() * magicNumber;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SMASH));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE_PER_EXHAUSTED);
            upgradeDescription();
        }
    }
}
