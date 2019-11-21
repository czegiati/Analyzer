package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import core.Const;

@Number(name="CONST",max=0)
public class ConstNum extends AbstractNumber implements Const {
    Integer value;
    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value=(Integer)value;
    }
}
