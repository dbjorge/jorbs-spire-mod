package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.ChainLightningAction;
import stsjorbsmod.characters.Wanderer;

import java.util.ArrayList;
import java.util.Iterator;

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
    private static final int UPGRADE_PLUS_PER_HOP = 2;

    public ChainLightning() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_PLUS_PER_HOP;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractMonster> targets = AbstractDungeon.getMonsters().monsters;
        addLightningEffect(targets.iterator());
        addToBot(new ChainLightningAction(p, m, targets, damage, magicNumber, AttackEffect.NONE));
    }

    private void addLightningEffect(Iterator monsterIterator) {
        AbstractMonster monster;
        float duration = 0F;
        int index = 0;
        while(monsterIterator.hasNext()) {
            monster = (AbstractMonster)monsterIterator.next();
            if (!monster.isDeadOrEscaped()) {
                duration += (0.1F * index);
                index += 1;
                AbstractDungeon.actionManager.addToBottom(new SFXAction("THUNDERCLAP", duration));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(monster.drawX, monster.drawY), duration));
            }
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_PER_HOP);
            initializeDescription();
        }
    }
}
