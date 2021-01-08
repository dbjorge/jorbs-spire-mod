package stsjorbsmod.cards.cull;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.actions.ConsumerGameAction;
import stsjorbsmod.actions.PermanentlyModifyDamageAction;
import stsjorbsmod.cards.CustomJorbsModCard;
import stsjorbsmod.cards.DeathPreventionCard;
import stsjorbsmod.cards.WasHPLostCardSubscriber;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.util.CardMetaUtils;
import stsjorbsmod.util.EffectUtils;

import static stsjorbsmod.JorbsMod.JorbsCardTags.LEGENDARY;

/**
 * Unplayable. NL Retain. NL When you die instead deal (3 +) all damage taken while this was in your hand to all enemies. NL stsjorbsmod:Destroy.
 */
public class ShriekingHat extends CustomJorbsModCard implements DeathPreventionCard, WasHPLostCardSubscriber, CustomSavable<Integer> {
    public static final String ID = JorbsMod.makeID(ShriekingHat.class);

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Cull.Enums.CULL_CARD_COLOR;

    private AbstractPlayer p = AbstractDungeon.player;
    private int priority;

    private static final int COST = COST_UNPLAYABLE;
    private static final int UPGRADE_DAMAGE = 3;

    public ShriekingHat() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);

        damage = baseDamage = misc = magicNumber = 0;
        selfRetain = true;

        tags.add(LEGENDARY);
    }

    @Override
    public int getPriority() {
        return priority;
    }

    /**
     * used in ShowCardAndAddToHandEffect::new which gets used in Discovery, Card Potions, and MakeTempCardInHandAction leading to Dead Branch
     */
    @Override
    public void triggerWhenCopied() {
        super.triggerWhenCopied();
        priority = currentPriority.incrementAndGet();
    }

    @Override
    public void wasHPLost(int damageAmount) {
        addToBot(new PermanentlyModifyDamageAction(uuid, damageAmount));
    }

    @Override
    public boolean canUse(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        this.cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
        return false;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new VFXAction(new ShowCardBrieflyEffect(this)));
        addToBot(new SFXAction("ATTACK_PIERCING_WAIL", -1, true));
        if (Settings.FAST_MODE) {
            addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.3F));
        } else {
            addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.RED_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 1.5F));
        }
        addToBot(new DamageAllEnemiesAction(p, baseDamage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        addToBot(new ConsumerGameAction<>(EffectUtils::showDestroyEffect, this));
        CardMetaUtils.destroyCardPermanently(this);
    }

    @Override
    public String getRawDynamicDescriptionSuffix() {
        return EXTENDED_DESCRIPTION[0];
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_DAMAGE);
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        ShriekingHat copy = (ShriekingHat) super.makeCopy();
        copy.priority = this.priority;
        return copy;
    }

    @Override
    public Integer onSave() {
        return priority;
    }

    @Override
    public void onLoad(Integer integer) {
        this.priority = integer;
    }
}
