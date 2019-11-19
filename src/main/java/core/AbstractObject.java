package core;

import java.util.ArrayList;
import java.util.List;

public interface AbstractObject {

    List<AbstractObject> subObjs = new ArrayList<>();

    default public List<? extends AbstractObject> getSubObjects(){
        return  subObjs;
    }

    default public void setSubObjects(List<? extends AbstractObject> objs){
        for(AbstractObject abs: objs){
           subObjs.add(abs);
        }
    }
    public abstract Object getValue();
}
