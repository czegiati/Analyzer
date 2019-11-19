package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;

@Number(name="ONE",max=0)
public class ONE extends AbstractNumber {
    @Override
    public Integer getValue() {
        return 1;
    }
}
