package stsjorbsmod.util;

import com.evacipated.cardcrawl.modthespire.Loader;
import javassist.Modifier;
import org.clapper.util.classutil.*;
import stsjorbsmod.JorbsMod;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {
    @SuppressWarnings("unchecked")
    public static <T> ArrayList<Class<T>> findAllConcreteJorbsModClasses(ClassFilter classFilter)
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
                            classFilter
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
            throw new RuntimeException("Exception while finding concrete subclasses", e);
        }
    }

    // Note: only works for base classes defined within our own jar (limitation of ClassFinder)
    public static <T> ArrayList<Class<T>> findAllConcreteJorbsModSubclasses(Class<T> baseJorbsModClass) {
        return findAllConcreteJorbsModClasses(new SubclassClassFilter(baseJorbsModClass));
    }

    // Note: only works for base classes defined within our own jar (limitation of ClassFinder)
    // Requires each class have a no-arg constructor
    public static <T> List<T> instantiateAllConcreteJorbsModSubclasses(Class<T> baseJorbsModClass) {
        ArrayList<Class<T>> classes = findAllConcreteJorbsModClasses(new SubclassClassFilter(baseJorbsModClass));
        return classes.stream().map(clazz -> {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Exception while instantiating " + clazz.getName() + " (no default ctor?)", e);
            }
        }).collect(Collectors.toList());
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

    public static <FieldT, InstanceT> void setPrivateField(InstanceT instance, Class<? super InstanceT> clz, String fieldName, FieldT newValue) {
        try {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, newValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <FieldT, InstanceT> void setFieldIfExists(InstanceT instance, Class<? super InstanceT> clz, String fieldName, FieldT newValue) {
        try {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, newValue);
        } catch(NoSuchFieldException e) {
            return; // Intentional no-op
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Method tryGetMethod(Class<?> clz, String methodName, Class<?>... paramTypes) {
        try {
            return clz.getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
