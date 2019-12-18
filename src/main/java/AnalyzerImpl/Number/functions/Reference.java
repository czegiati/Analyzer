package AnalyzerImpl.Number.functions;

import restrictions.HaveNoSuperElement;
import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import core.attributes.AttributeDependent;
import core.attributes.AttributeParam;
import core.attributes.Interceptor;

import java.util.HashMap;
import java.util.Map;


@AttributeDependent
@HaveNoSuperElement("REF")
@Number(name="REF")
public class Reference extends AbstractNumber {
    public static Map<String, Reference> functions=new HashMap<>();
    private AbstractNumber num;
    private String action;
    private String name;
    @Override
    public Integer getValue() {

        if(action.equals("get"))
        {
            return functions.get(name).getValue();
        }
        else // set
        {
            if(this.getSubObjects().size()!=1) throw new IllegalArgumentException("REF should have 1 arg when declaring it it!");
            this.num=this.getSubObjects().get(0);
            functions.put(name,this);
        }
        return num.getValue();
    }

    @Interceptor
    public void init( @AttributeParam("name")String name,@AttributeParam("action")String action){
        this.action=action;
        this.name=name;
        if(action.equals("set"))
        {
            //if(functions.containsKey(name))
             // throw new IllegalArgumentException("You can only declare an NFUNC once!");
        }
        else if(action.equals("get")){
            if(!functions.containsKey(name)) throw new IllegalArgumentException("Unknown REF: "+name);
        }else throw new IllegalArgumentException("You can only use action: set or get in REF!");
    }

}
