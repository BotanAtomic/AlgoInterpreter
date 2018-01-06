package org.algo.interpreter.handler;

import org.algo.interpreter.core.Algorithm;
import org.algo.interpreter.exception.InterpreterException;
import org.algo.interpreter.object.Variable;
import org.algo.interpreter.type.ParsableType;

import static org.algo.interpreter.utils.StringUtils.fullTrim;

public class DefaultHandler implements Handler {


    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {

        if (fullLine.contains("=")) {
            String[] setArgs = fullLine.split("=");
            String potentialVariable = fullTrim(setArgs[0]);

            if (algorithm.getVariables().get(potentialVariable) != null) {
                Variable variable = algorithm.getVariables().get(potentialVariable);
                if (setArgs.length == 2) {
                    ParsableType.assignNewValue(variable, line, algorithm, fullTrim(setArgs[1]));
                } else {
                    InterpreterException.invokeNew("Ligne " + line + " : format d'assignation incorrect", algorithm.getPrinter());
                }
            } else {
                InterpreterException.invokeNew("Ligne " + line + " : vous tentez d'assigner une valeur à une variable inconnue", algorithm.getPrinter());
            }
        }

        return true;
    }
}
