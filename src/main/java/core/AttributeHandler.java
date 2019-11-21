package core;

import java.util.Map;

@FunctionalInterface
public interface AttributeHandler {
    void handle(Object o, String name, Map<String,String> attributes,Map<String, Class> classmap);
}
