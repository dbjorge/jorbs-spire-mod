package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.ChainLightningAction;
import stsjorbsmod.characters.Wanderer;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class ChainLightning extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ChainLightning.class.getSimpleName());
    public static final String IMG = makeCardPath("AOE_Commons/chain_lightning.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int DAMAGE_PLUS_PER_HOP = 2;
    private static final int UPGRADE_PLUS_DMG = 2;

    public ChainLightning() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_PLUS_PER_HOP;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractMonster> targets = AbstractDungeon.getMonsters().monsters;
        enqueueAction(new ChainLightningAction(p, m, targets, damage, magicNumber, AttackEffect.SLASH_VERTICAL));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
