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
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class CoupDeGrace extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(CoupDeGrace.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = CustomJorbsModCard.COST_X;
    private static final int EXTRA_TARGET_INTANGIBLE = 0;
    private static final int UPGRADE_EXTRA_TARGET_INTANGIBLE = 1;

    public CoupDeGrace() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = EXTRA_TARGET_INTANGIBLE;
        tags.add(LEGENDARY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) {
            AbstractDungeon.player.getRelic(ChemicalX.ID).flash();
        }

        for (int i = 0; i < magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    protected int calculateBonusMagicNumber() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) {
            effect += 2;
        }

        if (this.upgraded) {
            effect += 1;
        }

        return effect;
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_EXTRA_TARGET_INTANGIBLE);
            upgradeDescription();
        }
    }
}
