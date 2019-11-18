package core;

import java.util.List;

public abstract class AbstractCondition {
    public List<AbstractCondition> subConditions;
    final public void setSubConditions(List<AbstractCondition> conds){
        this.subConditions=conds;
    }
    public abstract boolean getValue();
}
