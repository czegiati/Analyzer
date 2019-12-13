package AnalyzerImpl.Condition;

import Analyzer.core.AbstractObject;
import Analyzer.core.AttributeDependent.AttributeDependent;
import Analyzer.core.AttributeDependent.AttributeParam;
import Analyzer.core.AttributeDependent.Interceptor;
import Analyzer.core.content.Content;
import Analyzer.core.mixed.TypeBridge;
import Analyzer.core.parsers.ParseWith;
import AnalyzerImpl.Condition.functions.False;
import AnalyzerImpl.Condition.functions.True;
import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import AnalyzerImpl.Number.NumberAnalyzer;
import parsers.implementations.XMLParser;

public abstract class AbstractCondition extends AbstractObject<Boolean,AbstractCondition> {

    @Content
    static String content;

    @TypeBridge(name="ASD",analyzerClass = {})
    public static AbstractCondition asd(){
        System.out.println(content);
        return new True();
    }

    @TypeBridge(name = "EQUALS",analyzerClass = {NumberAnalyzer.class})
    public static AbstractCondition Equals(AbstractNumber num1,AbstractNumber num2){
        if(num1.getValue()==num2.getValue())
            return new True();
        else return new False();
    }

    @Interceptor("EQUALS")
    public static void asd(@AttributeParam(value = "i",required = false) String i){
        System.out.println("EQ");
    }

    @TypeBridge(name = "GT",analyzerClass = {NumberAnalyzer.class})
    public static AbstractCondition GreaterThan(AbstractNumber num1,AbstractNumber num2){
        if(num1.getValue()>num2.getValue())
            return new True();
        else return new False();
    }

    @Interceptor("GT")
    public static void gt(){
        System.out.println("GT");
    }


}
