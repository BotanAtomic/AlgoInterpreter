package org.algo.interpreter.handler;

import org.algo.interpreter.core.Algorithm;
import org.algo.interpreter.exception.InterpreterException;

public class HeaderHandler implements Handler {

    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {
        String[] arguments = fullLine.split(" ");

        if (arguments[0].toLowerCase().equals("algorithme")) {
            if (arguments.length > 1)
                algorithm.setName(fullLine.substring("algoritme".length() + 2));
            else
                algorithm.setName("Sans nom");

            algorithm.getPrinter().print("Debut de l'algoritme " + algorithm.getName() + "\n");
        } else
            InterpreterException.invokeNew("Ligne " + line + " : un algorithme doit commencer par le mot clé 'Algorithme'", algorithm.getPrinter());

        return true;
    }

}
