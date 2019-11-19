package AnalyzerImpl.Condition;

import core.AbstractObject;

import java.util.List;

public abstract class AbstractCondition implements AbstractObject {
    @Override
    public List<AbstractCondition> getSubObjects() {
        return this.getSubObjects();
    }

    @Override
    public Boolean getValue() {
        return null;
    }
}
