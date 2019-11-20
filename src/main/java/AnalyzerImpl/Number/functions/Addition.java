package AnalyzerImpl.Number.functions;


import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import core.AbstractObject;

@Number(name="ADD",min=2)
public class Addition extends AbstractNumber {
    @Override
    public Integer getValue() {
        int num=0;
        for(AbstractObject i:this.getSubObjects())
        {
            num+=(Integer) i.getValue();
        }
        return num;
    }
}
