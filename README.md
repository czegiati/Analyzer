
<p1><b>Analyzer 1.2.1</b></p1>
1,What is it for?
Implementing the Analyzer interface, you can easily define tags, and assign a function to them.
(Currently it only works for XML-files, but it is going to get extended in the future.)

Example for the xml:
```xml
<OR>
    <TRUE/>
    <FALSE>
    <NOT>
      <TRUE/>
    </NOT>
</OR>
```
For this example we have created 4 classes (TRUE,FALSE,NOT,OR), that are being used to assign functions to the different tags.

2,How to use?

Implement the Analyzer<Annotation,AClass> interface (Annotation is the annotation defined by you(int must contain a 'name()' attribute)
and the AClass is an Abstract class that implements AbstractObject)


AnalyzerImplementation example:
```
public class MyAnalyzer implements Analyzer<Condition, AbstractCondition> {
    @Override
    public Map<String, Class<AbstractCondition>> getAbstractClassMap() {
        return null;
    }

    @Override
    public boolean acceptAnnotationOn(Annotation annotation, Class<?> class0, List<AbstractCondition> subobjs) {
        return true;
    }

    @Override
    public Class<Condition> getAnnotationClass() {
        return Condition.class;
    }

    @Override
    public Class<AbstractCondition> getAbstractClass() {
        return AbstractCondition.class;
    }
}
```


Note: acceptAnnotationOn() can be used to implement restrictions on the functions (should return true when you accept ) example:
```
@Override
 public boolean acceptAnnotationOn(Annotation annotation, Class<?> class0, List<AbstractCondition> subobjs) {
     Integer min=null;
        Integer max=null;
        int argn=subobjs.size();
        try {
            min= (int) annotation.getClass().getMethod("min").invoke(annotation);
            max= (int) annotation.getClass().getMethod("max").invoke(annotation);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (min<=argn && max>=argn) || (min<=argn && max==-1);
}
```

Note: You should define the variables,you want to use as restrictions (in here the min and max methods of the Annotation), in the annotation:
```
public @interface Condition {
    int min() default 0;
    int max() default -1;
    String name();
}
```

Next is the getAbstractClassMap(), you should create a Map field in the class, and return it with the getAbstractClassMap, that you override:
```
    public Map<String,Class<AbstractCondition>> classmap=new HashMap<>();

@Override
    public Map<String, Class<YourAbstractClass>> getAbstractClassMap() {
        return classmap;
    }
```
Next is the getAnnotationClass and getAbstractClass, they should return yourAnnotation.class and yourAbstractClass.class:
```
@Override
    public Class<Condition> getAnnotationClass() {
        return Condition.class;
    }

    @Override
    public Class<AbstractCondition> getAbstractClass() {
        return AbstractCondition.class;
    }
```

The implementation of YourAbstractObject should look like this:
```
public abstract class MyAbstract implements AbstractObject {
    List<MyAbstract> list=new ArrayList<>();

    @Override
    public List<? extends AbstractObject> getSubObjects() {
        return list;
    }

    @Override
    public void setSubObjects(List<? extends AbstractObject> objs) {
        this.list=objs;
    }

    @Override
    public abstract XYZ getValue();
}
```
In this situation XYZ is supposed to represent the Object, that you want to return. The Condition and number implementation packages (in AnalyzerImpl.Condition and AnalyzerImpl.Number) return a Boolean and an Integer.
You should also create a list as shown example.


After this, you can start implementing your own functions:
```
@Condition(name="AND")
public class AndCondition extends AbstractCondition {

    @Override
    public Boolean getValue() {
        for(AbstractCondition cond:this.getSubObjects())
        {
            if(!(Boolean)cond.getValue()) return false;
        }
        return true;
    }

}
```
You register the class as a function by Implementing your own AbstractClass an and annotating it with @YourAnnotation, and giving it a name (in this example "AND").
Then you can use getValue to give it your desired behaviour.



  And finally use it:
```
    public static void main(String[] args) {
         Analyzer analyzer = new ConditionAnalyzer();
                analyzer.annotationDetect();
                System.out.println(XMLMetaParser.parse("xml.xml", analyzer).getValue());
    }
```
    xml.xml is:
        ```xml
        <OR>
            <TRUE/>
            <FALSE>
            <NOT>
              <TRUE/>
            </NOT>
        </OR>
        ```


<p1><b>NEW FEATURES IN 1.2 </b></p1>

You can implement in your function class the Const interface:
```
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
```

When implementing Const you should create a static Map<String,The type getValueReturns>, and use it as shown in this example.

Then in main:
```
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, InstantiationException {

        Analyzer analyzer = new NumberAnalyzer();
        analyzer.annotationDetect();
        analyzer.defineConst("10",new Integer(10),new ConstNum());
        analyzer.defineConst("12",new Integer(12),new ConstNum());
        analyzer.defineConst("20",new Integer(20),new ConstNum());
        System.out.println(XMLMetaParser.parse("xml.xml", analyzer).getValue());


    }
    ```

((12+20)*10) in xml.xml:
        ```xml
       <MUL>
           <ADD>
               <CONST-name-12></CONST-name-12>
               <CONST-name-20></CONST-name-20>
           </ADD>
           <CONST-name-10></CONST-name-10>
       </MUL>
        ```
Output: 320

