package stsjorbsmod.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.CharityMemory;

import static stsjorbsmod.JorbsMod.makeCardPath;
import static stsjorbsmod.characters.Wanderer.Enums.REMEMBER_MEMORY;

public class OldPocket extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(OldPocket.class.getSimpleName());
    public static final String IMG = makeCardPath("Damage_Uncommons/old_pocket.png");

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int GOLD_GAIN = 10;
    private static final int UPGRADE_PLUS_GOLD_GAIN = 4;

    public OldPocket() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = GOLD_GAIN;
        exhaust = true;

        this.tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.player.gainGold(magicNumber);
        enqueueAction(new RememberSpecificMemoryAction(new CharityMemory(p, false)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_GOLD_GAIN);
            initializeDescription();
        }
    }
}
