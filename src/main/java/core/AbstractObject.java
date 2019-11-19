package core;

import java.util.ArrayList;
import java.util.List;

public interface AbstractObject {

    List<AbstractObject> subObjs = new ArrayList<>();

    default public List<AbstractObject> getSubObjects(){
        return  subObjs;
    }

    default public void setSubObjects(List<? extends AbstractObject> objs){
        for(AbstractObject abs: objs){
            getSubObjects().add(abs);
        }
    }
    public abstract boolean getValue();
}
