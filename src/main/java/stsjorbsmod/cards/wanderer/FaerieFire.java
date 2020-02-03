package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.AbstractMemory;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.MemoryType;

public class FaerieFire extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(FaerieFire.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int DEBUFF_DURATION = 3;
    private static final int UPGRADE_PLUS_DEBUFF_DURATION = 2;

    public FaerieFire() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DEBUFF_DURATION;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster mo) {
        AbstractMemory currentMemory = MemoryManager.forPlayer(p).currentMemory;
        if (currentMemory == null) { return; }

        for(AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractPower debuff = currentMemory.memoryType == MemoryType.SIN ?
                    new VulnerablePower(m, magicNumber, false) :
                    new WeakPower(m, magicNumber, false);

            addToBot(new ApplyPowerAction(m, p, debuff));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DEBUFF_DURATION);
            upgradeDescription();
        }
    }
}
