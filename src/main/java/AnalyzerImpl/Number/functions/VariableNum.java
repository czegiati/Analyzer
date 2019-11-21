package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import core.Variable;

@Number(name="VAR",max=0)
public class VariableNum extends AbstractNumber implements Variable {

    private String name;

    @Override
    public Integer getValue() {
        return (Integer)map.get(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }


}
