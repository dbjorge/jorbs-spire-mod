package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ConsumptionAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Consumption extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Consumption.class);
    public static final Logger logger = LogManager.getLogger(Cull.class.getName());


    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = -1;
    private static final int DAMAGE = 1;

    public Consumption() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        this.isMultiDamage = true;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }

        if (this.upgraded) {
            effect += 1;
        }

        if (effect > 0) {
            this.baseDamage = effect;
            logger.info("New baseDamage is: " + this.baseDamage);
            logger.info("New damage is: " + this.damage);
            this.calculateCardDamage(abstractMonster);
            logger.info("Damage after calculation is: " + this.damage);
            this.addToBot(new ConsumptionAction(abstractMonster, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        }

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
