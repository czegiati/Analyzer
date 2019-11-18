package conditions;

import core.AbstractCondition;
import core.Condition;

@Condition(name="NOT",max=1,min=1)
public class NotCondition extends AbstractCondition {
    @Override
    public boolean getValue() {
        return !this.subConditions.get(0).getValue();
    }
}
