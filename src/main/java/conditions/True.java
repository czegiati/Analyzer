package conditions;

import condition.AbstractCondition;
import condition.Condition;

@Condition(max=0,name="TRUE")
public class True extends AbstractCondition {

    @Override
    public boolean getValue() {
        return true;
    }
}
