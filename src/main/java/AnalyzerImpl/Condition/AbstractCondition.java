package AnalyzerImpl.Condition;

import Analyzer.core.AbstractObject;
import Analyzer.core.mixed.TypeBridge;
import AnalyzerImpl.Condition.functions.False;
import AnalyzerImpl.Condition.functions.True;
import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.NumberAnalyzer;

public abstract class AbstractCondition extends AbstractObject<Boolean,AbstractCondition> {

    @TypeBridge(name = "EQUALS",analyzerClass = {NumberAnalyzer.class})
    public static AbstractCondition Equals(AbstractNumber num1,AbstractNumber num2){
        if(num1.getValue()==num2.getValue())
            return new True();
        else return new False();
    }

}
