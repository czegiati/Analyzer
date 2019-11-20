package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import core.AbstractObject;

@Number(name="MUL",min=2)
public class Multiply extends AbstractNumber {
    @Override
    public Integer getValue() {
        int num=1;
        for(AbstractObject a:getSubObjects()){
            num*=(Integer) a.getValue();
        }
        return num;
    }
}
