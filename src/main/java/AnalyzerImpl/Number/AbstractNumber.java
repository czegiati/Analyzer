package AnalyzerImpl.Number;

import core.AbstractObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNumber implements AbstractObject {
    List<AbstractNumber> nums=new ArrayList<>();
    @Override
    public List<AbstractNumber> getSubObjects() {
        return nums;
    }

    @Override
    public void setSubObjects(List<? extends AbstractObject> objs) {
        this.nums= (List<AbstractNumber>) objs;
    }

    @Override
    public abstract Integer getValue();
}
