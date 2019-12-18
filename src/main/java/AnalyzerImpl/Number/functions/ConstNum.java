package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import core.attributes.AttributeDependent;
import core.attributes.AttributeParam;
import core.attributes.Interceptor;

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


    public void setValue(Integer value) {
        this.value = value;
    }
}


