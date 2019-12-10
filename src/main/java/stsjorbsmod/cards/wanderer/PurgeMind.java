package stsjorbsmod.cards.wanderer;

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
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;
import static stsjorbsmod.JorbsMod.makeCardPath;

public class PurgeMind extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(PurgeMind.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE_PER_REMEMBER = 8;
    private static final int UPGRADE_DAMAGE = 3;

    public PurgeMind() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE_PER_REMEMBER;
        baseMagicNumber = 0;
        exhaust = true;
    }

    @Override
    public int calculateBonusMagicNumber() {
        AbstractPlayer p = AbstractDungeon.player;
        return countRememberCardsInGroup(p.hand) +
                countRememberCardsInGroup(p.drawPile) +
                countRememberCardsInGroup(p.discardPile) +
                countRememberCardsInGroup(p.exhaustPile);
    }

    private int countRememberCardsInGroup(CardGroup group) {
        return (int) group.group.stream().filter(c -> c.hasTag(REMEMBER_MEMORY)).count();
    }

    private void exhaustRememberCardsInGroup(CardGroup group) {
        for (AbstractCard card : group.group) {
            if (card.hasTag(REMEMBER_MEMORY)) {
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, group));
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        exhaustRememberCardsInGroup(p.hand);
        exhaustRememberCardsInGroup(p.drawPile);
        exhaustRememberCardsInGroup(p.discardPile);

        for (int i = 0; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY, false));
        }

        AbstractDungeon.actionManager.addToBottom(new SnapAction(p));
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return magicNumber == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1];
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
