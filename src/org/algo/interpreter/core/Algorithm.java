package org.algo.interpreter.core;

import org.algo.interpreter.object.Variable;
import org.algo.interpreter.printer.Printer;

import java.util.HashMap;
import java.util.Map;

public class Algorithm {
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
}
