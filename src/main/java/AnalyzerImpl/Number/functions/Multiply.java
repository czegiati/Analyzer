package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;

@Number(name="MUL",min=2)
public class Multiply extends AbstractNumber {
    @Override
    public Integer getValue() {
        int num=1;
        for(AbstractNumber a:getSubObjects()){
            num*= a.getValue();
        }
        return num;
    }
}
