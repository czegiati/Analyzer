package hu.unideb.czty.analyzer.core.fields;


import hu.unideb.czty.analyzer.parsers.classes.AnalyzerElement;
import java.lang.reflect.Field;

public interface AnalyzerElementSetter {
    public static void inject(Object instance, AnalyzerElement element) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(GetAnalyzerElement.class)) {
                field.setAccessible(true); // should work on private fields
                try {
                    field.set(instance, element);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void injectStatic(Class c, AnalyzerElement element) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(GetAnalyzerElement.class)) {
                field.setAccessible(true); // should work on private fields
                try {
                    field.set(null, element);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
