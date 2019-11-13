package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;

import static stsjorbsmod.JorbsMod.makeCardPath;

// Deal 3(4) Damage. Deal 3(4) Damage again once for each Clarity.
public class MagicMissiles extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(MagicMissiles.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Uncommons/magic_missiles.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 1;

    public MagicMissiles() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int numMissiles = 1 + MemoryManager.forPlayer(p).countCurrentClarities();

        for (int i=0; i<numMissiles; ++i) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.SMASH));
        }
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
