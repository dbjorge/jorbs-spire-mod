package stsjorbsmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.powers.memories.AbstractMemoryPower;
import stsjorbsmod.powers.memories.MemoryType;
import stsjorbsmod.util.MemoryPowerUtils;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class FocusedMind extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FocusedMind.class.getSimpleName());
    public static final String IMG = makeCardPath("Manipulation_Commons/focused_mind.png");

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

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
        AbstractMemoryPower currentMemory = MemoryPowerUtils.getCurrentMemory(p);

        if (currentMemory != null && currentMemory.memoryType == MemoryType.SIN) {
            enqueueAction(new DamageAction(m, new DamageInfo(p, damage), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }

        if (currentMemory != null && currentMemory.memoryType == MemoryType.VIRTUE) {
            enqueueAction(new GainBlockAction(p, p, block));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DAMAGE);
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            initializeDescription();
        }
    }
}
