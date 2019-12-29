package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.patches.EphemeralField;

public class Panic extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Panic.class);

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 0;
    private static final int INTANGIBLE_AMOUNT = 1;
    private static final int MAXHP_LOSS = 3;
    private static final int UPGRADE_MAXHPLOSS = -2;

    public Panic() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = INTANGIBLE_AMOUNT;
        urMagicNumber = baseUrMagicNumber = MAXHP_LOSS;
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, magicNumber)));
        AbstractDungeon.effectList.add(new FlashAtkImgEffect(p.hb.cX,p.hb.cY, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        AbstractDungeon.player.decreaseMaxHealth(urMagicNumber);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeUrMagicNumber(UPGRADE_MAXHPLOSS);
            upgradeDescription();
        }
    }
}
