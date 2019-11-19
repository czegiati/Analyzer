package AnalyzerImpl.Number.functions;


import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;

@Number(name="ADD",min=2)
public class Addition extends AbstractNumber {
    @Override
    public Integer getValue() {
        int num=0;
        for(AbstractNumber i:this.getSubObjects())
        {
            num+=i.getValue();
        }
        return num;
    }
}
