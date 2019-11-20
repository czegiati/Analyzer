package AnalyzerImpl.Condition.functions;


import AnalyzerImpl.Condition.AbstractCondition;
import AnalyzerImpl.Condition.Condition;
import core.AbstractObject;

@Condition(name="AND")
public class AndCondition extends AbstractCondition {

    @Override
    public Boolean getValue() {
        for(AbstractObject cond:this.getSubObjects())
        {
            if(!(Boolean)cond.getValue()) return false;
        }
        return true;
    }

}
