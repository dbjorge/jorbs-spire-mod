package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ConsumeCardAction;
import stsjorbsmod.actions.DamageWithOnKillEffectAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.powers.MirroredTechniquePower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.PERSISTENT_POSITIVE_EFFECT;

public class MirroredTechnique extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(MirroredTechnique.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int EXTRA_PLAYS = 0;
    private static final int UPGRADE_EXTRA_PLAYS = 1;

    public MirroredTechnique() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EXTRA_PLAYS;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new MirroredTechniquePower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_EXTRA_PLAYS);
            upgradeDescription();
        }
    }
}
