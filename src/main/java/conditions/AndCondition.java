package conditions;

import core.AbstractCondition;
import core.Condition;

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
