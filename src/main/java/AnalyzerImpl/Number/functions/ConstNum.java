package AnalyzerImpl.Number.functions;

import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import core.Const;

import java.util.HashMap;
import java.util.Map;

@Number(name="CONST",min=0,max=0)
public class ConstNum extends AbstractNumber implements Const {

    private static Map<String,Integer> map=new HashMap<>();
    private String name;

    @Override
    public Integer getValue() {
        return map.get(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public void put(String s, Object o) {
        this.name=s;
        map.put(s,(Integer)o);
    }

    @Override
    public Object get(String name) {
        return map.get(name);
    }

}
