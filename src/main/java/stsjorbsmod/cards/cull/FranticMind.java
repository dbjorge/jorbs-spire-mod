package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.ManifestPatch;

public class FranticMind extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FranticMind.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;

    public FranticMind() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.baseMagicNumber = 0;
    }

    @Override
    public int calculateBonusMagicNumber() {
        return ManifestPatch.PlayerManifestField.manifestField.get(AbstractDungeon.player);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        if (magicNumber > 0) {
            addToBot(new DiscardAction(p, p, magicNumber, false));
            addToBot(new DrawCardAction(magicNumber));
        }
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return magicNumber == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1];
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
            this.exhaust = false;
        }
    }
}
