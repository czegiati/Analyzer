1,What is it for?

This is a simple project, that makes the user capable of reading boolean expressions from a xml file. It has some basic functions, but it is easily expandable.
Basic functions: AND,OR,NOT,TRUE,FALSE.



2,How to use?
Basically all you have to do is calling ConditionAnalyzer.annotationDetect(); before using XMLParser.parse("file.xml").

    public static void main(String[] args) {
        ConditionAnalyzer.annotationDetect();
        XMLParser.parse("file.xml");
    }

You can also add new functions by extending AbstractCondition and annotating it with @Condition.

Condition has 3 fields:
min - minimum number of parameters (0 by default)
max - maximum number of parameters (unlimited by default)
name - You must give it a name in order to identify it


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

It will return the AbstractCondition class, and you can get its value by using the getValue() method.
