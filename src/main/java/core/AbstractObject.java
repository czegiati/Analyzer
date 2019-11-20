package core;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObject<T> {

    List<AbstractObject> objects=new ArrayList<>();
    public List<AbstractObject> getSubObjects(){
        return objects;
    }

    public void setSubObjects(List<AbstractObject> objs){
        this.objects=objs;
    }

    public abstract T getValue();
}
