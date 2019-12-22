package stsjorbsmod.cards.cull;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;

public class Taunt extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Taunt.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 2;
    private static final int INTANGIBLE = 2;
    private static final int VULNERABLE = 3;
    private static final int UPGRADE_PLUS_VULNERABLE = 5;

    public Taunt() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = VULNERABLE;
        urMagicNumber = baseUrMagicNumber = INTANGIBLE;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, urMagicNumber)));
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_VULNERABLE);
            upgradeDescription();
        }
    }
}
