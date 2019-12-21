package stsjorbsmod.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.monsters.MirrorImageMinion;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.makePowerPath;

public class MirrorImagePower extends AbstractPower implements OnDamageToRedirectSubscriber {
    public static final String POWER_ID = JorbsMod.makeID(MirrorImagePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("next_attack_misses_84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("next_attack_misses_32.png"));

    private MirrorImageMinion minion; // The power only maintains 1 minion; we change its max health along with our stacks

    public MirrorImagePower(AbstractCreature owner, int amountOfImages) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amountOfImages;

        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.updateDescription();
    }

    private MirrorImageMinion initializeMinion() {
        MirrorImageMinion minion = new MirrorImageMinion(this, this.amount);
        // -1 turns prevents it from displaying a turn duration. We don't worry about it expiring because we
        // intentionally don't set the minion up to receive onEndRound events.
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(minion, minion, new IntangiblePlayerPower(minion, -1)));
        minion.init();
        minion.showHealthBar();
        return minion;
    }

    @Override
    public void stackPower(int increase) {
        super.stackPower(increase);
        minion.increaseMaxHp(increase, true);
    }

    @Override
    public void onInitialApplication() {
        minion = initializeMinion();
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        if (this.minion != null) {
            this.minion.update();
            this.minion.hb.update();
            this.minion.healthHb.update();
        }
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, c);
        if (this.minion != null) {
            this.minion.render(sb);
        }
    }

    @Override
    public void onRemove() {
        minion.die();
        minion = null;
    }

    @Override
    public void updateDescription() {
        this.description = String.format(amount == 1 ? DESCRIPTIONS[0] : DESCRIPTIONS[1], amount);
    }

    @Override
    public boolean onDamageToRedirect(AbstractPlayer player, DamageInfo info, AttackEffect effect) {
        if (info.type == DamageType.NORMAL &&
            info.owner != null &&
            info.owner != player &&
            this.minion != null &&
            !this.minion.isDead)
        {
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.minion, info, effect));
            return true;
        }
        return false;
    }
}
