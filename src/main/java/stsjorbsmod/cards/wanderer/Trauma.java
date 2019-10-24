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
    private static final int DAMAGE = 30;
    private static final int UPGRADE_PLUS_DMG = 10;
    private static final int HP_LOSS = 5;

    public Trauma() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = HP_LOSS;
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
        // Phase 1
        addToBot(new SnapAction(p));

        // Phase 2
        addToBot(makeTransientLoseHPAction(p));
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.BLUNT_HEAVY));

        // Phase 3
        addToBot(makeTransientLoseHPAction(p));
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AttackEffect.BLUNT_HEAVY));
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
