package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.MemoryManager;

import static stsjorbsmod.JorbsMod.makeCardPath;

public class Mending extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Mending.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int HEAL_PER_CLARITY = 1;
    private static final int UPGRADE_PLUS_HEAL_PER_CLARITY = 1;

    public Mending() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        metaMagicNumber = baseMetaMagicNumber = HEAL_PER_CLARITY;
        baseMagicNumber = 0;
        exhaust = true;

        tags.add(CardTags.HEALING);
    }

    @Override
    public int calculateBonusMagicNumber() {
        return MemoryManager.forPlayer(AbstractDungeon.player).countCurrentClarities() * metaMagicNumber;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new HealAction(p, p, magicNumber));
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMetaMagicNumber(UPGRADE_PLUS_HEAL_PER_CLARITY);
            upgradeDescription();
        }
    }
}
