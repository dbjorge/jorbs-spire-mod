package stsjorbsmod.relics;

import basemod.BaseMod;
import basemod.abstracts.CustomRelic;
import basemod.interfaces.PostUpdateSubscriber;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.memories.MemoryManager;
import stsjorbsmod.memories.OnModifyMemoriesSubscriber;
import stsjorbsmod.powers.MindGlassPower;
import stsjorbsmod.util.TextureLoader;

import static stsjorbsmod.JorbsMod.*;
import static stsjorbsmod.characters.Wanderer.Enums.WANDERER_CARD_COLOR;

/**
 * When gaining a unique clarity, deals 5 damage to all enemies.
 * When gain the tenth clarity in a combat, deal 500 damage to all enemies.
 */
public class MindGlassRelic extends CustomJorbsModRelic implements OnModifyMemoriesSubscriber, PostUpdateSubscriber {
    public static final String ID = JorbsMod.makeID(MindGlassRelic.class);

    private static final int ONE_CLARITY_DAMAGE = 3;
    private static final int TEN_CLARITY_DAMAGE = 100;

    public MindGlassRelic() {
        super(ID, WANDERER_CARD_COLOR, RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void atPreBattle() {
        BaseMod.subscribe(this);
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onGainClarity(String id) {
        ++this.counter;
        this.flash();
        if (this.counter == 10) {
            this.stopPulse();
        } else if (this.counter == 9) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(
                            AbstractDungeon.player,
                            AbstractDungeon.player,
                            new MindGlassPower(AbstractDungeon.player, TEN_CLARITY_DAMAGE),
                            1,
                            true));
        }
        AbstractDungeon.actionManager.addToBottom(
                new DamageAllEnemiesAction(
                        (AbstractCreature) null,
                        DamageInfo.createDamageMatrix(ONE_CLARITY_DAMAGE, true),
                        DamageInfo.DamageType.NORMAL,
                        // TODO: More impactful and relevant FX. See FlashAtkImgEffect.loadImage() and
                        //  FlashAtkImgEffect.playSound() for usage of AttackEffect in base game.
                        AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void onVictory() {
        this.counter = -1;
        this.stopPulse();
        BaseMod.unsubscribe(this);
    }

    /**
     * After every game update, if this instance of the relic was subscribed to the post relic, check if it should pulse
     */
    @Override
    public void receivePostUpdate() {
        if (this.counter == 9 && !pulse && flashTimer == 0) {
            this.beginLongPulse();
        }
    }
}
