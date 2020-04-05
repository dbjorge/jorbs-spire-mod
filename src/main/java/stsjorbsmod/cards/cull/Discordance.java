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
import java.util.HashSet;
import java.util.Iterator;

public class Discordance extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Discordance.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE_PER_CARD_TYPE = 5;
    private static final int UPGRADE_ADDED_DAMAGE_PER_CARD_TYPE = 2;



    public Discordance() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = 0;
        this.baseMagicNumber = magicNumber = DAMAGE_PER_CARD_TYPE;
    }

    @Override
    public int calculateBonusBaseDamage() {
        HashSet<CardType> seen = new HashSet<CardType>();

        for(AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c != this) {
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
            upgradeMagicNumber(UPGRADE_ADDED_DAMAGE_PER_CARD_TYPE);
            upgradeName();
            upgradeDescription();
        }
    }
}
