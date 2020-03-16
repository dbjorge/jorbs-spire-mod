package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

import java.util.ArrayList;
import java.util.Iterator;

public class Discordance extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Discordance.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;



    public Discordance() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 0;
        this.baseMagicNumber = magicNumber = 5;
    }

    @Override
    public int calculateBonusBaseDamage() {
        Iterator var2 = AbstractDungeon.player.hand.group.iterator();
        ArrayList<CardType> seen = new ArrayList<CardType>();

        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (!seen.contains(c.type) && c.cardID != this.cardID) {
                seen.add(c.type);
            }
        }

        return seen.size() * magicNumber;
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.LIGHTNING));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeMagicNumber(2);
            upgradeName();
            upgradeDescription();
        }
    }
}
