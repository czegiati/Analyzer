package AnalyzerImpl.Condition.functions;


import AnalyzerImpl.Condition.AbstractCondition;
import AnalyzerImpl.Condition.Condition;
import core.AbstractObject;

@Condition(name="OR")
public class OrCondition extends AbstractCondition {

    @Override
    public Boolean getValue() {
        for(AbstractCondition cond:this.getSubObjects())
        {
            if( cond.getValue()) return true;
        }
        return false;
    }
}
