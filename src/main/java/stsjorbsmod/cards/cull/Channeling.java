package stsjorbsmod.cards.cull;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.util.IntentUtils;

public class Channeling extends CustomJorbsModCard {
    public static final String ID = JorbsMod.makeID(Channeling.class);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private static final int COST = 1;

    public Channeling() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        baseMagicNumber = 0;
    }

    @Override
    public int calculateBonusMagicNumber() {
        int channelCount = 0;
        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDying && m.currentHealth > 0 && !m.isEscaping) {
                if (IntentUtils.isAttackIntent(m.intent)) {
                    if ((boolean)ReflectionHacks.getPrivate(m, AbstractMonster.class, "isMultiDmg")) {
                        channelCount += (int)ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
                    } else {
                        ++channelCount;
                    }
                }
            }
        }
        return channelCount;
    }

    @Override
    public boolean shouldGlowGold() {
        return IntentUtils.playerCanSeeThatAnyEnemyIntentMatches(IntentUtils::isAttackIntent);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster abstractMonster) {
        for (int i = 0; i < magicNumber; ++i) {
            this.addToBot(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
        }
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        if (!IntentUtils.areIntentsHidden()) {
            return magicNumber == 1 ? EXTENDED_DESCRIPTION[0] : EXTENDED_DESCRIPTION[1];
        }
        return "";
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            this.exhaust = false;
            upgradeDescription();
        }
    }
}
