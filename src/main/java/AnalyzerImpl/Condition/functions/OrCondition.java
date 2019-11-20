package AnalyzerImpl.Condition.functions;


import AnalyzerImpl.Condition.AbstractCondition;
import AnalyzerImpl.Condition.Condition;
import core.AbstractObject;

@Condition(name="OR")
public class OrCondition extends AbstractCondition {

    @Override
    public Boolean getValue() {
        for(AbstractObject cond:this.getSubObjects())
        {
            if((Boolean) cond.getValue()) return true;
        }
        return false;
    }
}
