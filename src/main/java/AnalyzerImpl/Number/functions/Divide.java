package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;

@Number(name="DIV",min=2,max=2)
public class Divide extends AbstractNumber {
    @Override
    public Integer getValue() {
        return(Integer) getSubObjects().get(0).getValue()/ (Integer) getSubObjects().get(1).getValue();

    }
}
