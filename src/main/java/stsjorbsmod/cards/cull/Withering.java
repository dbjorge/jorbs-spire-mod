package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnDrawCardSubscriber;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.powers.WitheringPower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class Withering extends CustomJorbsModCard implements OnDrawCardSubscriber {
    public static final String ID = JorbsMod.makeID(Withering.class);

    private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.BASIC;
    private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;
    private static final AbstractCard.CardType TYPE = CardType.CURSE;
    public static final AbstractCard.CardColor COLOR = CardColor.CURSE;

    private static final int COST = 1;
    private static final int INTANGIBLE = 1;
    private static final int WITHERING_ON_DRAW = 1;

    public Withering() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = INTANGIBLE;
        baseMetaMagicNumber = metaMagicNumber = WITHERING_ON_DRAW;
        tags.add(LEGENDARY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber)));
    }

    @Override
    public void onDraw() {
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new ApplyPowerAction(p, p, new WitheringPower(p, metaMagicNumber)));
    }

    @Override
    public void upgrade() {
        // Curses can't upgrade
    }

}
