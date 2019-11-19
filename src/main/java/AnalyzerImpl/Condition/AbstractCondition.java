package AnalyzerImpl.Condition;

import core.AbstractObject;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCondition implements AbstractObject {
    List<AbstractCondition> conds=new ArrayList<>();

    @Override
    public List<AbstractCondition> getSubObjects() {
        return this.conds;
    }

    @Override
    public void setSubObjects(List<? extends AbstractObject> objs) {
        this.conds= (List<AbstractCondition>) objs;
    }

    @Override
    public abstract Boolean getValue();
}
