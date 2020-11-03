package stsjorbsmod.powers;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import stsjorbsmod.util.CardMetaUtils;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

public class DamnationPower extends CustomJorbsModPower implements NonStackablePower {
    public static final StaticPowerInfo STATIC = StaticPowerInfo.Load(DamnationPower.class);
    public static final String POWER_ID = STATIC.ID;
    private boolean isAutoUpgrade;

    public DamnationPower(final AbstractCreature owner, boolean isAutoUpgrade) {
        super(STATIC);

        this.owner = owner;
        this.isAutoUpgrade = isAutoUpgrade;

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.hasTag(LEGENDARY)) {
            return;
        }
        if (!card.purgeOnUse && StSLib.getMasterDeckEquivalent(card) != null) {
            CardMetaUtils.destroyCardPermanently(card);
            AbstractDungeon.transformCard(card, isAutoUpgrade, AbstractDungeon.miscRng);
            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(AbstractDungeon.getTransformedCard(), Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, false));
        }
        addToTop(new RemoveSpecificPowerAction(owner, owner, this.ID));
    }

    @Override
    public void updateDescription() {
        description = isAutoUpgrade ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new DamnationPower(owner, isAutoUpgrade);
    }
}
