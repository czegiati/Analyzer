package AnalyzerImpl.Condition.functions;


import AnalyzerImpl.Condition.AbstractCondition;
import AnalyzerImpl.Condition.Condition;

@Condition(max=0,name="TRUE")
public class True extends AbstractCondition {

    @Override
    public Boolean getValue() {
        return true;
    }
}
