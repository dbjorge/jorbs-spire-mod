package stsjorbsmod.cards.wanderer.materialcomponents;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.AdvanceRelicsThroughTimeAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Wanderer;
import stsjorbsmod.patches.EphemeralField;
import stsjorbsmod.powers.PlayNextCardThisTurnAdditionalTimesPower;

public class Quicksilver extends MaterialComponent {
    public static final String ID = JorbsMod.makeID(Quicksilver.class);

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Wanderer.Enums.WANDERER_CARD_COLOR;

    private static final int COST = 1;
    private static final int RELIC_COUNTER_INCREMENT = 1;
    private static final int NEXT_ATTACK_ADDITIONAL_TIMES = 1;
    private static final int UPGRADE_PLUS_NEXT_ATTACK_ADDITIONAL_TIMES = 1;

    public Quicksilver() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        bannerImageRarity = CardRarity.RARE;
        magicNumber = baseMagicNumber = NEXT_ATTACK_ADDITIONAL_TIMES;
        metaMagicNumber = baseMetaMagicNumber = RELIC_COUNTER_INCREMENT;
        EphemeralField.ephemeral.set(this, true);
    }

    @Override
    public void useMaterialComponent(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AdvanceRelicsThroughTimeAction(p, metaMagicNumber));
        addToBot(new ApplyPowerAction(p, p, new PlayNextCardThisTurnAdditionalTimesPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_NEXT_ATTACK_ADDITIONAL_TIMES);
            upgradeDescription();
        }
    }
}
