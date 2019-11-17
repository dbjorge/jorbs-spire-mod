package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
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

/**
 * 2: Deal 8 damage for all cards which remember in deck (including exhaust pile). Exhaust all cards which remember. Snap.
 */
public class PurgeMind extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(PurgeMind.class.getSimpleName());
    public static final String IMG = makeCardPath("Scaling_Rares/pyromancy.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int DAMAGE_PER_REMEMBER = 8;
    private static final int UPGRADE_DAMAGE = 3;

    public PurgeMind() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE_PER_REMEMBER;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int count = 0;
        for (AbstractCard card : p.hand.group) {
            if (card.tags.contains(REMEMBER_MEMORY)) {
                ++count;
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, p.hand));
            }
        }
        for (AbstractCard card : p.drawPile.group) {
            if (card.tags.contains(REMEMBER_MEMORY)) {
                ++count;
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, p.drawPile));
            }
        }
        for (AbstractCard card : p.discardPile.group) {
            if (card.tags.contains(REMEMBER_MEMORY)) {
                ++count;
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, p.discardPile));
            }
        }
        for (AbstractCard card : p.exhaustPile.group) {
            if (card.tags.contains(REMEMBER_MEMORY)) {
                ++count;
                AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(card, p.exhaustPile));
            }
        }
        for (int i = 0; i < count; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_HEAVY, false));
        }
        AbstractDungeon.actionManager.addToBottom(new SnapAction(p));
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
