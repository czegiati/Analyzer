package core;

import java.util.ArrayList;
import java.util.List;

public interface AbstractObject {

    public List<? extends AbstractObject> getSubObjects();

    public void setSubObjects(List<? extends AbstractObject> objs);

    public abstract Object getValue();
}
