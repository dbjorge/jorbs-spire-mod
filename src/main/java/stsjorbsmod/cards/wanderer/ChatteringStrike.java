package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.util.CardMetaUtils;

public class ChatteringStrike extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ChatteringStrike.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DAMAGE = -1;
    private static final int ADDITIONAL_PLAYS = 1;
    private static final int UPGRADE_PLUS_ADDITIONAL_PLAYS = 1;

    public ChatteringStrike() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = ADDITIONAL_PLAYS;
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.BLUNT_LIGHT));

        // Note: this avoids the extra copy generating another extra copy itself, but it also avoids copies from Gather
        // Power or Quicksilver generating additional extra copies (they're intended to only do one copy per stack)
        if (!this.purgeOnUse) {
            for (int i = 0; i < magicNumber; ++i) {
                CardMetaUtils.playCardAdditionalTime(this, m);
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeMagicNumber(UPGRADE_PLUS_ADDITIONAL_PLAYS);
            upgradeDescription();
        }
    }
}
