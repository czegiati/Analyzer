package Analyzer.core.functionalAttributes;

import Analyzer.core.AbstractObject;

public interface FunctionalAttributeHandler<T> {
    String getAttrbuteName();
    T handle(T object,String value);
}
