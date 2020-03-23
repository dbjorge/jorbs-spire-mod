package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class PollutedStrike extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(PollutedStrike.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE_PER_CURSE = 6;

    public PollutedStrike() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE_PER_CURSE;
        baseMagicNumber = 0;
        tags.add(CardTags.STRIKE);
    }

    @Override
    public int calculateBonusMagicNumber() {
        long cursesInHand = AbstractDungeon.player.hand.group
                .stream()
                .filter(c -> c.type.equals(CardType.CURSE))
                .count();
        return (int) cursesInHand;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.POISON));
        }
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {

        return magicNumber == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1];
    }


    @Override
    public void upgrade() {
    }
}
