package conditions;

import core.AbstractCondition;
import core.Condition;

@Condition(max=0,name="FALSE")
public class False extends AbstractCondition {
    @Override
    public boolean getValue() {
        return false;
    }
}
