package conditions;

import core.AbstractCondition;
import core.Condition;

@Condition(name="OR")
public class OrCondition extends AbstractCondition {

    @Override
    public boolean getValue() {
        for(AbstractCondition cond:this.subConditions)
        {
            if(cond.getValue()) return true;
        }
        return false;
    }
}
