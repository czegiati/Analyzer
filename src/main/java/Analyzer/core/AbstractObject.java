package Analyzer.core;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObject<T,C> {

    List<C> objects=new ArrayList<>();
    public List<C> getSubObjects(){
        return objects;
    }

    public void setSubObjects(List<C> objs){
        this.objects=objs;
    }

    public abstract T getValue();
}
