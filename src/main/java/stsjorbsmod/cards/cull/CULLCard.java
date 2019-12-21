package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.unique.RitualDaggerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.OnDrawCardSubscriber;
import stsjorbsmod.characters.Cull;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class CULLCard extends CustomJorbsModCard implements OnDrawCardSubscriber {
    public static final String ID = JorbsMod.makeID(CULLCard.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int DRAW_MINUS_DAMAGE = 1;
    private static final int KILL_PLUS_DAMAGE = 3;
    private static final int UPGRADE_PLUS_KILL_PLUS_DAMAGE = 1;

    public CULLCard() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        misc = baseDamage = DAMAGE;

        magicNumber = baseMagicNumber = KILL_PLUS_DAMAGE;
        urMagicNumber = baseUrMagicNumber = DRAW_MINUS_DAMAGE;
        exhaust = true;
        tags.add(LEGENDARY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new RitualDaggerAction(m, new DamageInfo(p, damage, damageTypeForTurn), magicNumber, uuid));
        this.addToBot(new RitualDaggerAction(m, new DamageInfo(p, damage, damageTypeForTurn), magicNumber, uuid));
    }

    @Override
    public void applyPowers() {
        baseBlock = misc;
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void onDraw() {
        addToBot(new ModifyDamageAction(uuid, -urMagicNumber));
    }

    @Override
    public void upgrade() {
        if(!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_KILL_PLUS_DAMAGE);
            upgradeDescription();
        }
    }
}
