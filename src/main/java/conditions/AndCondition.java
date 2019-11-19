package conditions;

import condition.AbstractCondition;
import condition.Condition;

@Condition(name="AND")
public class AndCondition extends AbstractCondition {

    @Override
    public boolean getValue() {
        for(AbstractCondition cond:this.subConditions)
        {
            if(!cond.getValue()) return false;
        }
        return true;
    }

}
