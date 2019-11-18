package conditions;

import core.AbstractCondition;
import core.Condition;

@Condition(max=0,name="TRUE")
public class True extends AbstractCondition {

    @Override
    public boolean getValue() {
        return true;
    }
}
