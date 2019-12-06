package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import Analyzer.core.AttributeDependent.AttributeDependent;
import Analyzer.core.AttributeDependent.AttributeParam;
import Analyzer.core.AttributeDependent.Interceptor;

@AttributeDependent
@Number(name="CONST",max=0)
public class ConstNum extends AbstractNumber {
    Integer value;
    @Override
    public Integer getValue() {
        return value;
    }


    @Interceptor
    public void init(@AttributeParam("value")String value){
        this.value=Integer.parseInt(value);
    }

}
