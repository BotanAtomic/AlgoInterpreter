package org.algo.interpreter.object;

public class Variable {

    private final String name;

    private Object value;

    public Variable(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public static boolean isCompatible(Variable root, Variable assign) {
        return root.getValue() instanceof Float || assign.getValue() instanceof Integer;
    }
}
