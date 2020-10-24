package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnCardExhumedSubscriber;
import stsjorbsmod.cards.OnEntombedSubscriber;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.powers.OldBookPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class OldBook extends CustomJorbsModCard implements OnCardExhumedSubscriber, OnEntombedSubscriber {
    public static final String ID = JorbsMod.makeID(OldBook.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private AbstractPlayer p = AbstractDungeon.player;

    private static final int COST = COST_UNPLAYABLE;
    private static final int HEAL_PERCENT = 0;
    private static final int UPGRADE_HEAL_PERCENT = 10;

    public OldBook() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        EntombedField.entombed.set(this, true);

        magicNumber = baseMagicNumber = HEAL_PERCENT;

        tags.add(LEGENDARY);
    }

    @Override
    public void onCardEntombed() {
        addToTop(new ApplyPowerAction(p, p, new OldBookPower(AbstractDungeon.player, this, 1)));
    }

    @Override
    public boolean canUse(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) { return false; }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
    }

    @Override
    public void onCardExhumed() {
        addToBot(new ReducePowerAction(p, p, OldBookPower.POWER_ID, 1));
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_HEAL_PERCENT);
            upgradeDescription();
        }
    }
}
