package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Toll extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Toll.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE_MULTIPLIER = 3;
    private static final int UPGRADE_PLUS_DAMAGE_MULTIPLIER = 1;

    public Toll() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = DAMAGE_MULTIPLIER;
        baseDamage = 0;
    }

    @Override
    protected int calculateBonusBaseDamage() {
        int bonusDamage = 0;
        for(AbstractRelic relic : AbstractDungeon.player.relics){
            if (relic.counter >= 0) {
                bonusDamage += relic.counter;
            }
        }
        return bonusDamage*magicNumber;
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        addToBot(new DamageAction(m, new DamageInfo(p, damage)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DAMAGE_MULTIPLIER);
            upgradeDescription();
        }
    }
}
