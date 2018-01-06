package org.algo.interpreter.handler;

import org.algo.interpreter.core.Algorithm;
import org.algo.interpreter.exception.InterpreterException;
import org.algo.interpreter.object.Variable;
import org.algo.interpreter.type.ParsableType;

import static org.algo.interpreter.utils.StringUtils.fullTrim;

public class VariableInitialization implements Handler {

    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {

        if (fullLine.equalsIgnoreCase("debut")) {
            return true;
        }

        String[] varArgs = fullLine.split(":");

        if (varArgs.length == 2) {

            String name = fullTrim(varArgs[0]);

            if (algorithm.getVariables().keySet().stream().anyMatch(n -> n.equalsIgnoreCase(name)))
                InterpreterException.invokeNew("Line : " + line + " : la variable " + name + " à déja été initialisée", algorithm.getPrinter());

            String type = fullTrim(varArgs[1]);
            Variable variable = new Variable(name);

            if (name.contains("[")) {
                int arraySize = getArraySize(name);

                if (name.contains("]") && arraySize != Integer.MIN_VALUE) {
                    variable.setValue(ParsableType.parseType(type, arraySize));
                } else {
                    InterpreterException.invokeNew("Line : " + line + " : mauvaise initialisation d'un tableau", algorithm.getPrinter());
                }
            } else {
                variable.setValue(ParsableType.parseType(type));
            }

            algorithm.addVariable(variable);

        } else {
            InterpreterException.invokeNew("Line : " + line + " : mauvaise structure d'initialisation de variable", algorithm.getPrinter());
        }
        return false;
    }

    private int getArraySize(String name) {
        try {
            return Integer.parseInt(name.substring(name.indexOf("[") + 1, name.indexOf("]")));
        } catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }
}
