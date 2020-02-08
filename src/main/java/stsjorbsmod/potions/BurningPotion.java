package stsjorbsmod.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.powers.BurningPower;

public class BurningPotion extends AbstractPotion {
    public static final String POTION_ID = JorbsMod.makeID(BurningPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    private static final int BASE_POTENCY = 8;

    public BurningPotion() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.BOTTLE, PotionColor.EXPLOSIVE);
        this.isThrown = true;
        this.targetRequired = true;
    }

    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = String.format(DESCRIPTIONS[0], this.potency);
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.add(new PowerTip(BaseMod.getKeywordTitle("stsjorbsmod:burning"), BaseMod.getKeywordDescription("stsjorbsmod:burning")));
    }

    @Override
    public void use(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(target, AbstractDungeon.player, new BurningPower(target, AbstractDungeon.player, potency)));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return BASE_POTENCY;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BurningPotion();
    }
}
