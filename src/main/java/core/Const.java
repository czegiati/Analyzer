package core;

import java.util.HashMap;
import java.util.Map;

public interface Const {
    String getName();
    void setName(String name);
     void put(String s, Object o);
     Object get(String name);
}
