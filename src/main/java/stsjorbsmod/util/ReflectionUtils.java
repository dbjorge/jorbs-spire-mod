package stsjorbsmod.util;

import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import javassist.Modifier;
import org.clapper.util.classutil.*;
import stsjorbsmod.JorbsMod;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;

public class ReflectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<Class<T>> findAllConcreteSubclasses(Class<T> baseClass)
    {
        try {
            ClassFinder finder = new ClassFinder();
            URL url = JorbsMod.class.getProtectionDomain().getCodeSource().getLocation();
            finder.add(new File(url.toURI()));

            ClassFilter filter =
                    new AndClassFilter(
                            new NotClassFilter(new InterfaceOnlyClassFilter()),
                            new NotClassFilter(new AbstractClassFilter()),
                            new ClassModifiersClassFilter(Modifier.PUBLIC),
                            new SubclassClassFilter(baseClass)
                    );
            ArrayList<ClassInfo> foundClassInfos = new ArrayList<>();
            finder.findClasses(foundClassInfos, filter);

            ArrayList<Class<T>> foundClasses = new ArrayList<>();
            for (ClassInfo classInfo : foundClassInfos) {
                Class<T> cls = (Class<T>) Loader.getClassPool().getClassLoader().loadClass(classInfo.getClassName());
                foundClasses.add(cls);
            }
            return foundClasses;
        } catch(Exception e) {
            throw new RuntimeException("Exception while finding concrete subclasses of " + baseClass.getName(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <FieldT, InstanceT> FieldT getPrivateField(InstanceT instance, Class<? super InstanceT> clz, String fieldName) {
        try {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (FieldT) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
