package stsjorbsmod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cutscenes.Cutscene;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class CutsceneMultiScreenPatch {
    @SpirePatch(
            clz = CutscenePanel.class,
            method = SpirePatch.CLASS
    )
    public static class CutscenePanelNewScreenField {
        public static SpireField<Boolean> startsNewScreen = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = Cutscene.class,
            method = "updateSceneChange"
    )
    public static class CutsceneFadeOlderPanelsOnNewScreen {
        public static void fadePanelsOnPreviousScreen(ArrayList<CutscenePanel> panels, CutscenePanel panelBeingActivated) {
            if (CutscenePanelNewScreenField.startsNewScreen.get(panelBeingActivated)) {
                for (CutscenePanel panel : panels) {
                    if (panel == panelBeingActivated) {
                        return;
                    }
                    if (!panel.fadeOut) {
                        panel.fadeOut();
                    }
                }
            }
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(CutscenePanel.class.getName()) && methodCall.getMethodName().equals("activate")) {
                        methodCall.replace(String.format(
                                "{ %1$s.fadePanelsOnPreviousScreen(this.panels, $0); $_ = $proceed($$); }",
                                CutsceneFadeOlderPanelsOnNewScreen.class.getName()));
                    }
                }
            };
        }
    }
}




