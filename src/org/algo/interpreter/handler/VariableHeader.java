package org.algo.interpreter.handler;

import org.algo.interpreter.core.Algorithm;

import static org.algo.interpreter.exception.InterpreterException.invokeNew;

public class VariableHeader implements Handler {


    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {
        if (!fullLine.equalsIgnoreCase("variables")) {
            invokeNew(
                    "Ligne " + line + " : vous devez indiquer le mot clé 'Variables' pour initialiser des variables", algorithm.getPrinter());
        }

        return true;
    }
}
