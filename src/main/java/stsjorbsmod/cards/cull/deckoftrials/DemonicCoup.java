package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

public class DemonicCoup extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(DemonicCoup.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int RITUAL_AMOUNT = 2;
    private static final int RITUAL_RETAIN_AMOUNT = 1;
    private static final int UPGRADE_RITUAL_AMOUNT = 1;

    public DemonicCoup() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        magicNumber = baseMagicNumber = RITUAL_AMOUNT;
        metaMagicNumber = baseMetaMagicNumber = RITUAL_RETAIN_AMOUNT;
        this.selfRetain=true;
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RitualPower(p, magicNumber, true)));
    }

    @Override
    public void onRetained() {
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RitualPower( AbstractDungeon.player, metaMagicNumber, true), metaMagicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_RITUAL_AMOUNT);
            upgradeDescription();
        }
    }
}
