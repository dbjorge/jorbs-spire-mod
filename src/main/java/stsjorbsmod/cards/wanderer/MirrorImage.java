package stsjorbsmod.cards.wanderer;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.actions.RememberSpecificMemoryAction;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.memories.KindnessMemory;
import stsjorbsmod.powers.MirrorImagePower;

import static stsjorbsmod.JorbsMod.JorbsCardTags.REMEMBER_MEMORY;

public class MirrorImage extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(MirrorImage.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 2;
    private static final int MINIONS = 1;
    private static final int UPGRADE_PLUS_MINIONS = 1;

    public MirrorImage() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MINIONS;
        exhaust = true;
        tags.add(REMEMBER_MEMORY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new RememberSpecificMemoryAction(p, KindnessMemory.STATIC.ID));
        addToBot(new ApplyPowerAction(p, p, new MirrorImagePower(p, magicNumber)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MINIONS);
            upgradeDescription();
        }
    }
}
