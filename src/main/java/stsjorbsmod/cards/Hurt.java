package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.util.MemoryPowerUtils;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Hurt extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Hurt.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Uncommons/hurt.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int DAMAGE = 14;
    private static final int UPGRADE_PLUS_DMG = 14;
    private static final int HP_LOSS_PER_CLARITY = 1;
    private static final int UPGRADE_PLUS_HP_LOSS_PER_CLARITY = 1;

    public Hurt() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = HP_LOSS_PER_CLARITY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));

        int hpLoss = MemoryPowerUtils.countClarities(p) * magicNumber;
        if (hpLoss > 0) {
            AbstractDungeon.actionManager.addToBottom(
                    new DamageAction(p, new DamageInfo(p, hpLoss, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SHIELD));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_HP_LOSS_PER_CLARITY);
            initializeDescription();
        }
    }
}
