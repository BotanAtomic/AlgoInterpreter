package org.algo.interpreter.handler;

import org.algo.interpreter.core.Algorithm;
import org.algo.interpreter.exception.InterpreterException;
import org.algo.interpreter.object.Variable;

public class PrintHandler implements Handler {


    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {
        String[] arguments = fullLine.split(" ");
        if (arguments.length > 1) {
            Variable variable = algorithm.getVariables().get(arguments[1]);

            if (variable != null) {
                algorithm.getPrinter().print(String.valueOf(variable.getValue()));
            } else {
                algorithm.getPrinter().print(fullLine.substring("afficher".length() + 1));
            }
        } else {
            InterpreterException.invokeNew("Ligne " + line + " : format de la fonction AFFICHER incorrect", algorithm.getPrinter());
        }

        return true;
    }
}
