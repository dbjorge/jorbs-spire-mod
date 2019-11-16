package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.util.TextureLoader;

import java.util.Arrays;
import java.util.List;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class FlameWardPower extends AbstractPower {
    public static final String POWER_ID = JorbsMod.makeID(FlameWardPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("placeholder_power84.png"));
    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("placeholder_power32.png"));

    private static final List<AbstractMonster.Intent> ATTACK_INTENTS = Arrays.asList(
            AbstractMonster.Intent.ATTACK,
            AbstractMonster.Intent.ATTACK_BUFF,
            AbstractMonster.Intent.ATTACK_DEBUFF,
            AbstractMonster.Intent.ATTACK_DEFEND);

    public FlameWardPower(AbstractPlayer owner, int amount) {
        super();
        this.name = NAME;
        this.ID = POWER_ID;
        this.type = PowerType.DEBUFF;

        this.owner = owner;
        this.amount = amount;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.updateDescription();
    }

    public void preDamage(DamageInfo info) {
        if (info != null && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && this.owner != info.owner) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.owner.hb.cX, this.owner.hb.cY, AbstractGameAction.AttackEffect.SHIELD));
            this.owner.addBlock(this.amount);
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (ATTACK_INTENTS.contains(m.intent)) {
                    AbstractDungeon.actionManager.addToTop(
                            new ApplyPowerAction(m, this.owner, new BurningPower(info.owner, this.owner, this.amount), 1, AbstractGameAction.AttackEffect.FIRE));
                }
            }
            this.flash();
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, this.amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = String.format(DESCRIPTIONS[0], this.amount, this.amount);
    }
}
