package stsjorbsmod.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

//@SpirePatch(
//        clz = CampfireUI.class,
//        method = "initializeButtons"
//)
//public class CampfireThirstPatch {
//    public CampfireThirstPatch() {
//    }
//
//    public static ExprEditor Instrument() {
//       return new ExprEditor() {
//           public void edit(NewExpr e) throws CannotCompileException {
//               if(e.getClassName().equals("com.megacrit.cardcrawl.ui.campfire.RestOption")) {
//                    e.replace("{$_ = new stsjorbsmod.campfire.ThirstOption(true);}");
//               }
//           }
//       };
//    }
//}
