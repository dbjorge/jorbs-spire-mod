package stsjorbsmod.cards.cull.deckoftrials;

import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.patches.SelfExertField;

public class Investment extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Investment.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int GOLD_PER_DRAW = 10;
    private static final int UPGRADE_GOLD_PER_DRAW = 2;


    public Investment() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = GOLD_PER_DRAW;
        this.misc = 0;
        this.baseMetaMagicNumber = 0;
        this.metaMagicNumber = this.baseMetaMagicNumber;

        EphemeralField.ephemeral.set(this, true);
        SelfExertField.selfExert.set(this, true);
    }

    @Override
    public void triggerWhenDrawn() {
        this.addToBot(new IncreaseMiscAction(this.uuid, this.misc, this.magicNumber));
    }

    @Override
    public void applyPowers() {
        this.baseMetaMagicNumber = this.misc;
        super.applyPowers();
        this.initializeDescription();
    }

    @Override
    public void applyLoadedMiscValue(int misc) {
        this.baseMetaMagicNumber = this.misc;
        super.applyLoadedMiscValue(misc);
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        AbstractDungeon.effectList.add(new RainingGoldEffect(this.metaMagicNumber * 2, true));
        AbstractDungeon.player.gainGold(this.metaMagicNumber);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_GOLD_PER_DRAW);
            upgradeDescription();
        }
    }
}
