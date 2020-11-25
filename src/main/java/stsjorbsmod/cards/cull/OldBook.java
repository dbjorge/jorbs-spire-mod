package stsjorbsmod.cards.cull;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.DeathPreventionCard;
import stsjorbsmod.cards.OnCardExhumedSubscriber;
import stsjorbsmod.cards.OnEntombedSubscriber;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EntombedField;
import stsjorbsmod.powers.OldBookPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class OldBook extends CustomJorbsModCard implements OnCardExhumedSubscriber, OnEntombedSubscriber, DeathPreventionCard, CustomSavable<Integer> {
    public static final String ID = JorbsMod.makeID(OldBook.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private final AbstractPlayer p = AbstractDungeon.player;
    private int priority;

    private static final int COST = COST_UNPLAYABLE;
    private static final int HEAL_PERCENT = 0;
    private static final int UPGRADE_HEAL_PERCENT = 10;

    public OldBook() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        EntombedField.entombed.set(this, true);

        magicNumber = baseMagicNumber = HEAL_PERCENT;

        tags.add(LEGENDARY);
    }

    /**
     * used in ShowCardAndAddToHandEffect::new which gets used in Discovery, Card Potions, and MakeTempCardInHandAction (Dead Branch and console command HandAdd)
     */
    @Override
    public void triggerWhenCopied() {
        super.triggerWhenCopied();
        priority = currentPriority.incrementAndGet();
    }

    /**
     * used in FastCardObtainEffect::update (combat reward cards) and ShowCardAndObtainEffect::update (just about every other card acquisition method events/transforms/etc).
     * overloading the meaning of shrink() because lazy.
     */
    @Override
    public void shrink() {
        super.shrink();
        priority = currentPriority.incrementAndGet();
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
    public int getPriority() {
        return priority;
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_HEAL_PERCENT);
            upgradeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        OldBook copy = (OldBook) super.makeCopy();
        copy.priority = this.priority;
        return copy;
    }

    @Override
    public Integer onSave() {
        return priority;
    }

    @Override
    public void onLoad(Integer integer) {
        this.priority = integer;
    }
}
