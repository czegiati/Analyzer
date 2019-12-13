package AnalyzerImpl.Number.functions;

import Analyzer.core.parsers.ParseWith;
import AnalyzerImpl.Number.AbstractNumber;
import AnalyzerImpl.Number.Number;
import parsers.implementations.XMLParser;

@ParseWith(parser = XMLParser.class)
@Number(name="PARSE")
public class Parsed extends AbstractNumber {
    @Override
    public Integer getValue() {
        return null;
    }
}
