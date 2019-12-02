package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.powers.MustClarifyBeforeRememberingNewMemoriesPower;

public class RefuseToForget extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(RefuseToForget.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 0;
    private static final int DRAW = 1;
    private static final int UPGRADE_PLUS_DRAW = 1;

    public RefuseToForget() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = DRAW;
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        MemoryManager mm = MemoryManager.forPlayer(p);
        if (mm != null && mm.currentMemory != null && !mm.currentMemory.isClarified) {
            addToBot(new ApplyPowerAction(p, p, new MustClarifyBeforeRememberingNewMemoriesPower(p, mm.currentMemory.ID)));
        }

        addToBot(new DrawCardAction(p, magicNumber));
        addToBot(new ApplyPowerAction(p, p, new NoDrawPower(p)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            upgradeDescription();
        }
    }
}
