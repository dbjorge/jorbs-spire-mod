package stsjorbsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.patches.DamageAsBurningPatch;
import stsjorbsmod.util.TextureLoader;

import java.util.ArrayList;

import static stsjorbsmod.JorbsMod.makePowerPath;

// attacks with random targets (specifically, uses of the AttackDamageRandomEnemyAction) target this enemy.
// This is primarily a marker power for applyPossibleActionTargetOverride to look for.
public class BlackTentaclesPower extends AbstractPower implements CloneablePowerInterface, AtEndOfPlayerTurnSubscriber {
    public AbstractCreature source;

    public static final String POWER_ID = JorbsMod.makeID(BlackTentaclesPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("black_tentacles_power84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("black_tentacles_power32.png"));

    public BlackTentaclesPower(final AbstractCreature owner, final AbstractCreature source) {
        ID = POWER_ID;
        this.name = NAME;
        this.type = PowerType.DEBUFF;

        this.owner = owner;
        this.source = source;
        this.amount = -1; // non-stackable

        this.isTurnBased = true;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        // only 1 target can have this power at a time; subsequent uses of the card will overwrite the old effect
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            for (AbstractPower p : m.powers) {
                if (p.ID.equals(this.ID) && p != this) {
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(m, source, p));
                }
            }
        }
    }

    @Override
    public void atEndOfPlayerTurn() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new BlackTentaclesPower(owner, source);
    }

    public int onAnyMonsterHpLoss(AbstractMonster monster, DamageInfo originalDamageInfo, int originalHpLoss) {
        if (monster != owner && originalDamageInfo != null && originalDamageInfo.owner == source && originalHpLoss > 0) {
            this.flash();
            DamageType newDamageType = originalDamageInfo.type == DamageType.HP_LOSS ? DamageType.HP_LOSS : DamageType.THORNS;
            DamageInfo newDamageInfo = new DamageInfo(source, originalHpLoss, newDamageType);
            DamageAsBurningPatch.isBurningField.isBurning.set(newDamageInfo, DamageAsBurningPatch.isBurningField.isBurning.get(originalDamageInfo));
            AbstractDungeon.actionManager.addToTop(new DamageAction(owner, newDamageInfo, AttackEffect.SLASH_DIAGONAL));
            return 0;
        }
        return originalHpLoss;
    }
}

