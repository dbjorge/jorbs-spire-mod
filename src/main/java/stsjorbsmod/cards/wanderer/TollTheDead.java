package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.MonsterLastDamagedOnTurnField;

public class TollTheDead extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(TollTheDead.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_PLUS_DMG = 2;

    public TollTheDead() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SMASH));

        final boolean monsterWasDamagedThisTurn = MonsterLastDamagedOnTurnField.lastDamagedOnTurn.get(m) == AbstractDungeon.actionManager.turn;
        if (monsterWasDamagedThisTurn) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SMASH));
        }
    }

    @Override
    public boolean shouldGlowGold() {
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream().anyMatch(m ->
                !m.isDeadOrEscaped() &&
                MonsterLastDamagedOnTurnField.lastDamagedOnTurn.get(m) == AbstractDungeon.actionManager.turn);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeDescription();
        }
    }
}
