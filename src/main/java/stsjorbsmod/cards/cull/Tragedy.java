package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class Tragedy extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Tragedy.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE_PER_CURSE = 10;
    private static final int UPGRADE_DAMAGE_PER_CURSE = 5;

    public Tragedy() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        damage = baseDamage = DAMAGE_PER_CURSE;
        magicNumber = baseMagicNumber = 0;
        exhaust = true;
        tags.add(LEGENDARY);
    }

    private int countCursesInGroup(CardGroup group) {
        return (int) group.group.stream().filter(c -> c.type.equals(CardType.CURSE)).count();
    }

    private void exhaustCursesInGroup(CardGroup group) {
        for (AbstractCard c : group.group) {
            if (c.type.equals(CardType.CURSE)) {
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(c, group));
            }
        }
    }

    @Override
    public int calculateBonusMagicNumber() {
        AbstractPlayer p = AbstractDungeon.player;
        return countCursesInGroup(p.hand) +
                countCursesInGroup(p.drawPile) +
                countCursesInGroup(p.discardPile) +
                countCursesInGroup(p.exhaustPile);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        exhaustCursesInGroup(p.hand);
        exhaustCursesInGroup(p.drawPile);
        exhaustCursesInGroup(p.discardPile);

        for (int i = 0; i < magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return magicNumber == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1];
    }


    @Override
    public void upgrade() {
        if(!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE_PER_CURSE);
            upgradeDescription();
        }
    }
}
