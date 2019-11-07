package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ChainLightningAction;
import stsjorbsmod.cards.CustomJorbsModCard;
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
    private static final int UPGRADE_PLUS_PER_HOP = 2;

    private int damageMultiplier = 0;

    public ChainLightning() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = DAMAGE_PLUS_PER_HOP;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractMonster> targets = getRandomOrderMonsters(AbstractDungeon.getMonsters().monsters, m);
        int[] damagesArray = getDamagesArray(targets);
        addToBot(new ChainLightningAction(p, m, targets, damagesArray, magicNumber, AttackEffect.NONE));
    }

    private ArrayList<AbstractMonster> getRandomOrderMonsters(ArrayList<AbstractMonster> targets, AbstractMonster initialTarget) {
        ArrayList<AbstractMonster> finalTargets = new ArrayList<>();
        ArrayList<AbstractMonster> newTargets = new ArrayList<>();

        newTargets.addAll(targets);
        int nextTargetIndex = newTargets.indexOf(initialTarget);
        while(true) {
            finalTargets.add(newTargets.remove(nextTargetIndex));
            if (newTargets.isEmpty()) break;
            nextTargetIndex = AbstractDungeon.cardRandomRng.random(0, newTargets.size() - 1);
        }
        return finalTargets;
    }

    private int[] getDamagesArray(ArrayList<AbstractMonster> targets) {
        int[] damagesArray = targets.stream().map(monster -> {
            if (monster.halfDead || monster.isDying || monster.isEscaping) return 0;
            int monsterDamage = getDamage(monster, damageMultiplier);
            damageMultiplier += 1;
            return monsterDamage;
        }).mapToInt(i -> i).toArray();

        damageMultiplier = 0;

        return damagesArray;
    }

    private int getDamage(AbstractMonster monster, int multiplier) {
        this.baseDamage = DAMAGE + (DAMAGE_PLUS_PER_HOP * multiplier);
        this.calculateCardDamage(monster);
        return this.damage;
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
