package core;

import java.util.HashMap;
import java.util.Map;

public interface Variable {
    Map<String,Object> map=new HashMap<>();

     default void put(String s, Object o){
         map.put(s,o);
     }

     default Object get(String name){
         return map.get(name);
     }

    String getName();

    void setName(String name);
}
