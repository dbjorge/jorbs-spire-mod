package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.SnapAction;
import stsjorbsmod.characters.Wanderer;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Trauma extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Trauma.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Rares/trauma.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 3;
    private static final int DAMAGE = 18;
    private static final int HP_LOSS = 5;
    private static final int ITERATIONS = 2;
    private static final int UPGRADE_PLUS_ITERATIONS = 1;

    public Trauma() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = HP_LOSS;
        metaMagicNumber = baseMetaMagicNumber = ITERATIONS;
        isMultiDamage = true;
    }

    private AbstractGameAction makeTransientLoseHPAction(AbstractPlayer p) {
        AbstractGameAction action = new LoseHPAction(p, p, magicNumber);
        // Using anything that isn't ActionType.DAMAGE here causes actionManager.clearPostCombatActions to remove
        // the action when a DamageAction/DamageAllEnemiesAction causes all the monsters to end up basically dead.
        action.actionType = AbstractGameAction.ActionType.SPECIAL;
        return action;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SnapAction(p));

        for (int i = 0; i < metaMagicNumber; ++i) {
            addToBot(makeTransientLoseHPAction(p));
            addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.BLUNT_HEAVY));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_ITERATIONS);
            upgradeDescription();
        }
    }
}
