package core.fields;


import parsers.classes.AnalyzerElement;

import java.lang.reflect.Field;

public interface ContentSetter {
    public static void inject(Object instance, AnalyzerElement element) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Content.class)) {
                field.setAccessible(true); // should work on private fields
                try {
                    field.set(instance, element.getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void injectStatic(Class c, AnalyzerElement element) {
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Content.class)) {
                field.setAccessible(true); // should work on private fields
                try {
                    field.set(null, element.getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
