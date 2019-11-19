package conditions;

import condition.AbstractCondition;
import condition.Condition;

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
