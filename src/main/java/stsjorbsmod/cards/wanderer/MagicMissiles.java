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
import stsjorbsmod.memories.MemoryManager;

import static stsjorbsmod.JorbsMod.makeCardPath;

// Deal 3(4) Damage. Deal 3(4) Damage again once for each Clarity.
public class MagicMissiles extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(MagicMissiles.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 1;
    private static final int BASE_NUMBER_HITS = 1;

    public MagicMissiles() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        baseMagicNumber = BASE_NUMBER_HITS;
    }

    @Override
    public int calculateBonusMagicNumber() {
        return MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SMASH));
        }
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return magicNumber == 1 ? cardStrings.EXTENDED_DESCRIPTION[0] : cardStrings.EXTENDED_DESCRIPTION[1];
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
