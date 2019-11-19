package conditions;

import condition.AbstractCondition;
import condition.Condition;

@Condition(max=0,name="FALSE")
public class False extends AbstractCondition {
    @Override
    public boolean getValue() {
        return false;
    }
}
