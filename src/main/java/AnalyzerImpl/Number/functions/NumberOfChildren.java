package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Condition.ConditionAnalyzer;
import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import AnalyzerImpl.Number.NumberAnalyzer;
import core.fields.GetAnalyzerElement;
import core.parsing.ParserIgnoresChildren;
import parsers.classes.AnalyzerElement;
import parsers.implementations.XMLParser;

@ParserIgnoresChildren
@Number(name = "NUM_OF_CHILDREN",min=1,max=1)
public class NumberOfChildren extends AbstractNumber {

@GetAnalyzerElement
AnalyzerElement element;

    @Override
    public Integer getValue() {
        if( (Boolean)new XMLParser().parseRoot(element.getSubElements().get(0),new ConditionAnalyzer()).getValue())
        {
            return 1;
        }
        return 0;
    }
}
