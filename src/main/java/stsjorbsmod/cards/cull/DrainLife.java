package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.DrainLifeAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.SelfExertField;

import static stsjorbsmod.JorbsMod.JorbsCardTags.PERSISTENT_POSITIVE_EFFECT;

public class DrainLife extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(DrainLife.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = CustomJorbsModCard.COST_X;

    public DrainLife() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 0;
        SelfExertField.selfExert.set(this, true);
        tags.add(PERSISTENT_POSITIVE_EFFECT);
    }

    @Override
    public int calculateBonusBaseDamage() {
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
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        if (AbstractDungeon.player.hasRelic(ChemicalX.ID)) {
            AbstractDungeon.player.getRelic(ChemicalX.ID).flash();
        }

        this.addToBot(new DrainLifeAction(abstractMonster, new DamageInfo(p, this.damage, this.damageTypeForTurn)));

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDescription();
        }
    }
}
