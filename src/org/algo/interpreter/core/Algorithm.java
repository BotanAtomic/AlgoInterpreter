package org.algo.interpreter.core;

import org.algo.interpreter.object.Variable;
import org.algo.interpreter.printer.Printer;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Algorithm {
    private Map<Integer,String> lines = new TreeMap<>();

    private int skipTo;

    private final Map<String, Variable> variables = new HashMap<>();

    private String name;

    private final Printer printer;

    Algorithm(Printer printer) {
        this.printer = printer;
    }

    public void addVariable(Variable variable) {
        this.variables.put(variable.getName(), variable);
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Printer getPrinter() {
        return printer;
    }

    public Map<Integer, String> getLines() {
        return lines;
    }

    public int getSkipTo() {
        return skipTo;
    }

    public void setSkipTo(int skipTo) {
        this.skipTo = skipTo;
    }
}
