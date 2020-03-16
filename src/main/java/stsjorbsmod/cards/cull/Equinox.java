package stsjorbsmod.cards.cull;

import basemod.interfaces.StartActSubscriber;
import basemod.interfaces.StartGameSubscriber;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Equinox extends CustomJorbsModCard implements StartActSubscriber, StartGameSubscriber {
    public static final String ID = JorbsMod.makeID(Equinox.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int UPGRADE_COST_REDUCTION = 1;

    public Equinox() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        determineCost();
        this.baseDamage = 0;
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public int calculateBonusBaseDamage() {
        return AbstractDungeon.floorNum;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            this.cost = AbstractDungeon.actNum - UPGRADE_COST_REDUCTION;
            setCostForTurn(cost);

            upgradeName();
            upgradeDescription();
        }
    }

    private void determineCost(){
        this.cost = AbstractDungeon.actNum;
        if (this.upgraded) {
            this.cost = AbstractDungeon.actNum - UPGRADE_COST_REDUCTION;
        }
        setCostForTurn(cost);
    }

    @Override
    public void receiveStartAct() {
        determineCost();
    }

    @Override
    public void receiveStartGame() {
        determineCost();
    }
}
