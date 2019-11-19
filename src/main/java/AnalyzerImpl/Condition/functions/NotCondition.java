package AnalyzerImpl.Condition.functions;


import AnalyzerImpl.Condition.AbstractCondition;
import AnalyzerImpl.Condition.Condition;

@Condition(name="NOT",max=1,min=1)
public class NotCondition extends AbstractCondition {
    @Override
    public Boolean getValue() {
        return getSubObjects().get(0).getValue();
    }
}
