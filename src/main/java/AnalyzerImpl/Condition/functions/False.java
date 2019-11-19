package AnalyzerImpl.Condition.functions;


import AnalyzerImpl.Condition.AbstractCondition;
import AnalyzerImpl.Condition.Condition;

@Condition(max=0,name="FALSE")
public class False extends AbstractCondition {
    @Override
    public Boolean getValue() {
        return false;
    }
}
