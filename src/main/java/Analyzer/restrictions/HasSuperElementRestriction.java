package Analyzer.restrictions;

import Analyzer.restrictions.core.RestrictionHandler;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

public class HasSuperElementRestriction implements RestrictionHandler {
    @Override
    public void accept(Object o, Map<String, String> attrs, List<String> superElements, Map<Integer, List<String>> subElements,Annotation annotation) {
        HasSuperElement anno=((HasSuperElement)annotation);
        if(anno.level()==-1){
            if(superElements.contains(anno.value())) return;
        }
        else if(anno.level()>=0){
            if(superElements.size()<=anno.level())
            {
                if(superElements.get(anno.level()).equals(anno.value())){
                    return;
                }
            }
        }


        throw new IllegalArgumentException(this.getClass()+" restrictions were not satisfied in "+o.getClass());
    }

}
