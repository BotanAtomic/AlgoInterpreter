package org.algo.interpreter.handler;

import org.algo.interpreter.core.Algorithm;
import org.algo.interpreter.exception.InterpreterException;
import org.algo.interpreter.object.Variable;
import org.algo.interpreter.type.ParsableType;

import java.util.Scanner;

public class InputHandler implements Handler {


    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {
        String[] arguments = fullLine.split(" ");
        if (arguments.length == 2) {
            Variable variable = algorithm.getVariables().get(arguments[1]);

            if (variable != null) { //TODO : generic scanner
                Scanner scanner = new Scanner(System.in);

                while (scanner.hasNext()) {
                    String value = scanner.nextLine();
                    ParsableType.assignNewValue(variable, line, algorithm, value);
                    break;
                }

            } else {
                InterpreterException.invokeNew("Ligne " + line + " : variable " + arguments[1] + " introuvable", algorithm.getPrinter());
            }
        } else {
            InterpreterException.invokeNew("Ligne " + line + " : format de la fonction SAISIR incorrect", algorithm.getPrinter());
        }

        return true;
    }
}
