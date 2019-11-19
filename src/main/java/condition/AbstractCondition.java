package condition;

import core.AbstractObject;

import java.util.List;

public abstract class AbstractCondition implements AbstractObject {
    public List<AbstractCondition> subConditions;
    final public void setSubConditions(List<AbstractCondition> objs){
        for(AbstractCondition abs: objs){
            subConditions.add(abs);
        }
    }
    public abstract boolean getValue();
}
