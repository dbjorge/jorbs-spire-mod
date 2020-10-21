package stsjorbsmod.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import stsjorbsmod.patches.CampfireThirstPatch;
import stsjorbsmod.patches.WrathField;

import static stsjorbsmod.JorbsMod.makeID;

public class CampfireThirstEffect extends AbstractGameEffect {
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID("ThirstEffect")).TEXT;

    private Color screenColor;
    private boolean openedScreen = false;
    private int healPerWrathStack;
    private int maxHPPerWrathStack;

    public CampfireThirstEffect() {
        this.screenColor = AbstractDungeon.fadeColor.cpy();
        this.duration = 1.5F;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();

        // Did this in the constructor rather than the update in case there are relics
        // for Cull that tweak these numbers.
        this.healPerWrathStack = 5;
        this.maxHPPerWrathStack = 1;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float) Settings.WIDTH, (float)Settings.HEIGHT);
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GRID) {
            AbstractDungeon.gridSelectScreen.render(sb);
        }
    }

    public void update() {
        if (!AbstractDungeon.isScreenUp) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.updateBlackScreenColor();
        }

        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                int wrathStacks = WrathField.wrathEffectCount.get(c);
                int healAmount = wrathStacks * this.healPerWrathStack;
                int maxHPIncreaseAmount = wrathStacks * this.maxHPPerWrathStack;

                WrathField.wrathEffectCount.set(c, 0);
                WrathField.updateCardDamage(c, -wrathStacks);
                AbstractDungeon.player.heal(healAmount);
                AbstractDungeon.player.increaseMaxHp(maxHPIncreaseAmount, true);
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
            }

            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            ((RestRoom)AbstractDungeon.getCurrRoom()).fadeIn();
        }

        if (this.duration < 1.0F && !this.openedScreen) {
            this.openedScreen = true;

            // Currently this is piggy backing off of the Toke / purge grid screen as its UX is close enough for a v1
            // could probably stand to patch GridCardSelectScreen to make it a bit friendlier
            AbstractDungeon.gridSelectScreen.open(CampfireThirstPatch.getWrathCards(),1, TEXT[0], false, false, true, true);
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
            if (CampfireUI.hidden) {
                AbstractRoom.waitTimer = 0.0F;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                ((RestRoom)AbstractDungeon.getCurrRoom()).cutFireSound();
            }
        }
    }

    public void dispose() {}

    private void updateBlackScreenColor() {
        if (this.duration > 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - 1.0F) * 2.0F);
        } else {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration / 1.5F);
        }
    }
}
