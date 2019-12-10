
<p1><b>ANALYZER 1.2.1</b></p1>

1,What is it for?

By implementing the Analyzer interface, you can easily define tags, and assign a function to them.

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
This will return TRUE since ( true || false || !true ) = true;

2,How to use?

Implement the Analyzer<AnnotationC,AbstrClass> interface (AnnotationC is the annotation defined by you(int must contain a 'name()' attribute)
and the AbstrClass is an Abstract class that implements AbstractObject)

Analyzer implementation example:
```
public class MyAnalyzer implements Analyzer<Condition, AbstractCondition> {

static Map<String,Class> map=new HashMap<>();    //you should create this static map

    @Override
    public Map<String, Class<AbstractCondition>> getAbstractClassMap() {  //you should return the static map given above
        return map;
    }

    @Override
    public boolean acceptAnnotationOn(Map annotation, Class<?> class0, List<AbstractCondition> subobjs) {
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



acceptAnnotationOn() can be used to implement restrictions on the functions (should return true when you accept ).
 This function gets called, when a object is getting instantiated.


 The paramaters:

 annotation= is a Map<String,Object> where the string is the fields name in your annotation

 class0= The actual class that is being instantiated tha will extend your AbstractClass

 subobjs= the already instantiated objects, that are under the current



The following example belongs to the ConditionAnalyzer. The Condition annotation have 3 fields in total: name(),min(),max().
Name is already a requirement,but min() and max() is not. In this example the method acceptAnnotationOn(...)
only allows instantiations of an object when:

<ul><li>the number of subobjects is between the min() and max() values </li>
  <li>the number of subobjects is equals or greater than min()</li></ul>

[note: min() is 0 by default and max() is -1 (which indicates that it is unlimited)]

```
@Override
 public boolean acceptAnnotationOn(Map annotation, Class<?> class0, List<AbstractCondition> subobjs) {
        Integer min =(int) annotation.get("min");
        Integer max = (int)annotation.get("max");
        int argn = subobjs.size();
        return (min <= argn && max >= argn) || (min <= argn && max == -1);
```

the condition annotation:

```
public @interface Condition {
    int min() default 0;
    int max() default -1;
    String name();
}
```

Next is the getAbstractClassMap(), you should create a Map field in the class, and return it with the getAbstractClassMap, that you override:
```
    public static Map<String,Class<AbstractCondition>> classmap=new HashMap<>();

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
public abstract class MyAbstract extends AbstractObject<MyObject,MyAbstract> {

}
```
MyObject is the object that gets returned from every classes getValue() extending MyAbstract.
For example the AbstractNumber's look like this:

```
public abstract class AbstractNumber extends AbstractObject<Integer,AbstractNumber> {

}
```



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
                analyzer.detect();  // detect(String path); is also a way to use the detector, in which case you can specify the exact place, where you want the Analyzer to look for annotated classes
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


<p1><b>ATTRIBUTES </b></p1>

You can also use attributes:

```
@AttributeDependent
@Number(name="CONST",max=0)
public class ConstNum extends AbstractNumber {
    Integer value;
    @Override
    public Integer getValue() {
        return value;
    }


    @Interceptor
    public void init(@AttributeParam("value")String value){
        this.value=Integer.parseInt(value);
    }


    public void setValue(Integer value) {
        this.value = value;
    }
}
```

You must inform the Analyzer that this class uses attributes by annotating it with @AttributeDependent and also by annotating a SINGLE method with @Interceptor.
The @Interceptor allows you to use the attributes.

In this example is init(@AttributeParam("value")String value) you get the attribute called "value", and store it in the String.




<p1><b>CONVERSION BETWEEN ANALYZERS </b></p1>

You can also make a converter between analyzers.

```
public abstract class AbstractCondition extends AbstractObject<Boolean,AbstractCondition> {

    @TypeBridge(name = "EQUALS",analyzerClass = {NumberAnalyzer.class})
    public static AbstractCondition Equals(AbstractNumber num1,AbstractNumber num2){
        if(num1.getValue()==num2.getValue())
            return new True();
        else return new False();
    }
}
```

The conversions can only be achieved by a public static method inside the AbstractClass and annotate it with @TypeBridge.
@TypeBridge have 2 attributes: name and analyzerClass. analyzerClass should contain the classes of the Analyzers that are able to recognize the parameters of the method.
(in this case NumberAnalyzer.class is there because its getAbstractClass() returns AbstractNumber.class)

And the name() attribute is used as a tag, and works just like the AndCondition class when we annotated it with @Condition(name="AND").

Note: It should return an object, that represents a constant value.



<p1><b>ADDITIONAL RESTRICTIONS </b></p1>

If you are not satisfied with the restrictions that has been presented to you, don't worry, there is more!

Although this mechanism is a still imperfect, it gives a lot of flexibility with your restrictions.

```
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@RestrictionAnnotation(HaveNoSuperElementRestriction.class)
public @interface HaveNoSuperElement {
    String value();

}
```

You can use the meta annotation @RestrictionAnnotation to create new restrictions! It has one attribute, which can be used to register the class that belongs to the annotation.
The registered class must implement the RestricitionHandler interface.

```
public class HaveNoSuperElementRestriction implements RestrictionHandler {
    @Override
    public void accept(Object o, Map<String, String> attrs, List<String> superElements, Map<Integer, List<String>> subElements, Annotation annotation) {
        if(superElements.contains(((HaveNoSuperElement)annotation).value()))
        {
            throw new IllegalArgumentException("Should not have super element "+(((HaveNoSuperElement)annotation).value())+"!");
        }
    }
}
```

The implementation should then throw an error, whenever your restriction is being violated. The accept(...) methods parameters:
<ul>
<li>Object o= the object being inspected by the restriction</li>
<li>Map attr = attributes, the keyset contains the names and the valueset contains the values</li>
<li>List superElements = the super elements going upwards from the current element</li>
<li>Map<Integer,List<String>> subElements= contains the names of the subelements; the keyset means the level; level-0 arethe children of the current object</li>
<li>Annotation annotation= the annotation that belongs to the current object </li>
</ul>


Before using it, you must call Analyzer.registerRestrictions(...) static method in order to register it!

<p1><b>PROTOTYPE FEATURES</b></p1>

There is only one feature that is under evaluation: the parser. When implemented, you are be able to parse any markup language, just like you can do it with xml.

It is finished, and even have an XML implementation, but it is 2-3 times slower, than the original parser. You can find it under parser.implementations.XMLParser.
It functions the same way as XMLMetaParser, so avoid using it, if you can.


