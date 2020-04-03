package stsjorbsmod.toppanel;

import basemod.BaseMod;
import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import stsjorbsmod.JorbsMod;
import stsjorbsmod.characters.Cull;
import stsjorbsmod.memories.WrathMemory;
import stsjorbsmod.patches.ManifestPatch;
import stsjorbsmod.twitch.SlayTheRelicsIntegration;

import java.util.ArrayList;

public class ManifestTopPanelItem extends TopPanelItem {
    public static final String ID = JorbsMod.makeID(ManifestTopPanelItem.class);
    private static final Texture IMG = new Texture(JorbsMod.makeImagePath("ui/manifest_top_panel_icon.png"));
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;

    private static ManifestTopPanelItem singleton;
    private static boolean isSingletonVisible = false;
    public static void show() {
        if (!isSingletonVisible) {
            if (singleton == null) { singleton = new ManifestTopPanelItem(); }
            BaseMod.addTopPanelItem(singleton);
            isSingletonVisible = true;
        }
    }
    public static void hide() {
        if (isSingletonVisible) {
            BaseMod.removeTopPanelItem(singleton);
            isSingletonVisible = false;
        }
    }

    private static float TIP_Y = (float)Settings.HEIGHT - 120.0F * Settings.scale;
    private static float TOP_RIGHT_TIP_X = 1550.0F * Settings.scale;

    private ArrayList<PowerTip> nonCullTips = new ArrayList<>();
    private ArrayList<PowerTip> cullTips = new ArrayList<>();
    private boolean isHovered = false;

    private ManifestTopPanelItem() {
        super(IMG, ID);
        this.setClickable(false);

        PowerTip manifestTip = new PowerTip(BaseMod.getKeywordTitle("stsjorbsmod:manifest"), BaseMod.getKeywordDescription("stsjorbsmod:manifest"));
        nonCullTips.add(manifestTip);

        cullTips.add(manifestTip);
        cullTips.add(new PowerTip(TEXT[0], TEXT[1]));
        cullTips.add(new PowerTip(TEXT[2], TEXT[3]));
        cullTips.add(new PowerTip(TEXT[4], TEXT[5], WrathMemory.STATIC.CLARITY_IMG_48));
    }

    @Override
    protected void onClick() { }

    @Override
    protected void onHover() {
        super.onHover();
        this.isHovered = true;
    }

    @Override
    protected void onUnhover() {
        super.onUnhover();
        this.isHovered = false;
    }

    @Override
    public void render(SpriteBatch sb, Color color) {
        super.render(sb, color);
        renderManifestNumberOverIcon(sb);
        renderTips();
    }

    private void renderManifestNumberOverIcon(SpriteBatch sb) {
        int manifest = AbstractDungeon.player == null ? 0 : ManifestPatch.PlayerManifestField.manifestField.get(AbstractDungeon.player);

        FontHelper.renderFontRightTopAligned(
                sb,
                FontHelper.topPanelAmountFont,
                Integer.toString(manifest),
                this.x + 58.0F * Settings.scale,
                this.y + 25.0F * Settings.scale,
                Color.WHITE.cpy());
    }

    private void renderTips() {
        ArrayList<PowerTip> tips = AbstractDungeon.player != null && AbstractDungeon.player instanceof Cull ? cullTips : nonCullTips;
        if (this.isHovered) {
            TipHelper.queuePowerTips(Math.min(this.x, TOP_RIGHT_TIP_X), TIP_Y, tips);
        }

        SlayTheRelicsIntegration.renderTipHitbox(this.hitbox, tips);
    }
}
