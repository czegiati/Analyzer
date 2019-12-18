package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import core.attributes.AttributeDependent;
import core.attributes.AttributeParam;
import core.attributes.Interceptor;

import java.util.HashMap;
import java.util.Map;

@AttributeDependent
@Number(name="VAR",max=0)
public class VariableNum extends AbstractNumber  {

    private String name;
    private static Map<String,Integer> vars=new HashMap<>();

    @Override
    public Integer getValue() {
        return vars.get(name);
    }


    @Interceptor
    public void init(@AttributeParam("name")String name){
        this.name=name;
    }

    public static void declareNewVariable(String s,Integer i){
        vars.put(s,i);
    }

}
