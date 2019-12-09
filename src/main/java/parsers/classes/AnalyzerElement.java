package parsers.classes;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AnalyzerElement {
    String name;
    AnalyzerElement parent;
    List<AnalyzerElement> subElements=new ArrayList<>();
    Map<String,String> attributes=new HashMap<>();

    public AnalyzerElement getParent() {
        return parent;
    }

    public void setParent(AnalyzerElement parent) {
        this.parent = parent;
    }

    public List<AnalyzerElement> getSubElements() {
        return subElements;
    }

    public void setSubElements(List<AnalyzerElement> subElements) {
        this.subElements = subElements;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AnalyzerElement> getSuperElements(){
        List<AnalyzerElement> list=new ArrayList<>();
        AnalyzerElement current=this.parent;
        while(current!=null){
            list.add(current);
            current=current.parent;
        }
        return list;
    }

    public Map<Integer,List<String>> getNamesOfSubElements(){
        Map<Integer,List<String>> subTags=new HashMap<>();
        List<AnalyzerElement> toBeExamined=this.getSubElements();
        int level=0;
        while(!toBeExamined.isEmpty()){
            List<String> tags=new ArrayList<>();
            List<AnalyzerElement> newElements=new ArrayList<>();
            for(AnalyzerElement e: toBeExamined)
            {
                tags.add(e.getName());
                newElements.addAll(e.getSubElements());
            }
            subTags.put(level,tags);
            toBeExamined=newElements;
            level++;
        }
        return subTags;
    }

}
