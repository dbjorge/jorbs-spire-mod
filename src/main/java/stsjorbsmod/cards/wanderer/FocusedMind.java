package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.MemoryType;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class FocusedMind extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FocusedMind.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Commons/focused_mind.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_GRAY_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DAMAGE = 3;
    private static final int BLOCK = 9;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public FocusedMind() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseBlock = BLOCK;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMemory currentMemory = MemoryManager.forPlayer(p).currentMemory;
        boolean isSnapped = MemoryManager.forPlayer(p).isSnapped();
        boolean rememberingSin = currentMemory != null && currentMemory.memoryType == MemoryType.SIN;
        boolean rememberingVirtue = currentMemory != null && currentMemory.memoryType == MemoryType.VIRTUE;

        if (rememberingSin || isSnapped) {
            addToBot(new DamageAction(m, new DamageInfo(p, damage), AttackEffect.BLUNT_LIGHT));
        }

        if (rememberingVirtue || isSnapped) {
            addToBot(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            upgradeDescription();
        }
    }
}
