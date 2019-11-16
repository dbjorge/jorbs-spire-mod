package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.BurningPower;

import static stsjorbsmod.JorbsMod.makeCardPath;

/**
 * BASE: Deal 10 damage. Apply 3 Burning. Heal for the amount of Burning on target. Lose 3 Max HP. Exhaust.
 * UPGRADE: Deal 12 damage. Apply 6 Burning. Heal for the amount of Burning on target. Lose 2 Max HP. Exhaust.
 */

public class ThirstingSword extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(ThirstingSword.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Rares/thirsting_sword.png");

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int BURNING = 3;
    private static final int LOSE_MAX_HP = 3;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int UPGRADE_BURNING = 3;
    private static final int UPGRADE_LOSE_MAX_HP = -1;

    public ThirstingSword() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = BURNING;
        metaMagicNumber = baseMetaMagicNumber = LOSE_MAX_HP;

        exhaust = true;

        tags.add(CardTags.HEALING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.FIRE)); //Deal Damage
        addToBot(new ApplyPowerAction(m, p, new BurningPower(m, p, BURNING))); //Apply Burning
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower possibleExistingBurningPower = m.getPower(BurningPower.POWER_ID);  //Get burning amount currently on target
                if (possibleExistingBurningPower != null) {
                    int healAmount = possibleExistingBurningPower.amount;
                    addToBot(new HealAction(p, p, healAmount)); //Heal for total burning on target
                }
                isDone = true;
            }
        });

        int loseMaxHPAmount = -metaMagicNumber;
        AbstractDungeon.player.increaseMaxHp(loseMaxHPAmount, true); //Lose MAX HP
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_BURNING);
            upgradeMetaMagicNumber(UPGRADE_LOSE_MAX_HP);
        }
    }
}
