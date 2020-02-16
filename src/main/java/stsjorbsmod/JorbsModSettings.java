package stsjorbsmod;

import basemod.*;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import stsjorbsmod.audio.VoiceoverMaster;

import java.util.Properties;

import static stsjorbsmod.JorbsMod.makeID;

public class JorbsModSettings {
    private static Properties DEFAULT_SETTINGS = new Properties();
    private static final String VOICEOVER_VOLUME_SETTING = "voiceover_volume";
    private static final String CULL_ENABLED_SETTING = "cull_enabled";
    static {
        DEFAULT_SETTINGS.setProperty(VOICEOVER_VOLUME_SETTING, "0.5");
        DEFAULT_SETTINGS.setProperty(CULL_ENABLED_SETTING, "false");
    }

    private static final String MOD_SETTINGS_FILE = "stsjorbsmod_config";

    private static SpireConfig config;

    public static boolean isCullEnabled() {
        return config.getBool(CULL_ENABLED_SETTING);
    }

    public static void initialize() {
        try {
            config = new SpireConfig(JorbsMod.MODNAME, MOD_SETTINGS_FILE, DEFAULT_SETTINGS);
            config.load();
            VoiceoverMaster.VOICEOVER_VOLUME = config.getFloat(VOICEOVER_VOLUME_SETTING);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void onVoiceoverVolumeChange(ModSlider slider) {
        try {
            VoiceoverMaster.VOICEOVER_VOLUME = slider.value;
            config.setFloat(VOICEOVER_VOLUME_SETTING, slider.value);
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onCullToggle(ModToggleButton toggle) {
        try {
            config.setBool(CULL_ENABLED_SETTING, toggle.enabled);
            config.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ModPanel createSettingsPanel() {
        String[] MOD_SETTINGS_PANEL_TEXT = CardCrawlGame.languagePack.getUIString(makeID("ModSettingsPanel")).TEXT;
        ModPanel settingsPanel = new ModPanel();

        ModLabel voiceoverVolumeSliderLabel = new ModLabel(
                MOD_SETTINGS_PANEL_TEXT[0],
                400.0F,
                694.0F,
                Color.WHITE,
                FontHelper.tipHeaderFont,
                settingsPanel,
                (label) -> {});
        settingsPanel.addUIElement(voiceoverVolumeSliderLabel);

        ModSlider voiceoverVolumeSlider = new ModSlider(
                "",
                700.0F,
                700.0F,
                100,
                "%",
                settingsPanel,
                JorbsModSettings::onVoiceoverVolumeChange);
        voiceoverVolumeSlider.setValue(VoiceoverMaster.VOICEOVER_VOLUME);
        settingsPanel.addUIElement(voiceoverVolumeSlider);

        ModLabeledToggleButton cullToggle = new ModLabeledToggleButton(
                MOD_SETTINGS_PANEL_TEXT[1],
                400.0F,
                650.0F,
                Color.WHITE,
                FontHelper.tipHeaderFont,
                isCullEnabled(),
                settingsPanel,
                (label) -> {},
                JorbsModSettings::onCullToggle);
        settingsPanel.addUIElement(cullToggle);

        ModLabel resetTipsLabel = new ModLabel(
                MOD_SETTINGS_PANEL_TEXT[2],
                400.0F,
                575.0F,
                Color.WHITE,
                FontHelper.tipHeaderFont,
                settingsPanel,
                (label) -> {});
        settingsPanel.addUIElement(resetTipsLabel);
        ModButton resetTipsButton = new ModButton(
                700.0F,
                520.0F,
                settingsPanel,
                (b) -> { JorbsModTipTracker.reset(); }
        );
        settingsPanel.addUIElement(resetTipsButton);

        return settingsPanel;
    }
}
