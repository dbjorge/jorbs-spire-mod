package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.util.UniqueCardUtils;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Mania extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Mania.class.getSimpleName());
    public static final String IMG = makeCardPath("Bad_Uncommons/mania.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 0;
    private static final int UPGRADE_PLUS_BASE_DMG = 3;
    private static final int DAMAGE_PER_UNIQUE_CARD = 1;
    private static final int ALL_UNIQUE_ENERGY = 1;
    private static final int ALL_UNIQUE_DRAW = 1;

    public Mania() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_PER_UNIQUE_CARD;
        metaMagicNumber = baseMetaMagicNumber = ALL_UNIQUE_ENERGY;
        urMagicNumber = baseUrMagicNumber = ALL_UNIQUE_DRAW;
    }

    @Override
    public int calculateBonusBaseDamage() {
        return UniqueCardUtils.countUniqueCards(AbstractDungeon.player.hand) * magicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SLASH_VERTICAL));

        if (UniqueCardUtils.countUniqueCards(AbstractDungeon.player.hand) == p.hand.size()) {
            addToBot(new GainEnergyAction(metaMagicNumber));
            addToBot(new DrawCardAction(p, urMagicNumber));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_BASE_DMG);
            upgradeDescription();
        }
    }
}
