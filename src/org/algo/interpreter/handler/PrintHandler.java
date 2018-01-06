package org.algo.interpreter.handler;

import org.algo.interpreter.core.Algorithm;
import org.algo.interpreter.object.Variable;

import java.util.concurrent.atomic.AtomicReference;

public class PrintHandler implements Handler {


    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {
        String[] arguments = fullLine.split(" ");
        if (arguments.length > 1) {
            Variable variable = algorithm.getVariables().get(arguments[1]);

            if (variable != null) {
                algorithm.getPrinter().print(String.valueOf(variable.getValue()));
            } else {
                AtomicReference<String> opex = new AtomicReference<>(fullLine.substring("afficher".length() + 1));
                algorithm.getVariables().forEach((key, value) -> opex.set(opex.get().replaceAll(key, String.valueOf(value.getValue()))));
                algorithm.getPrinter().print(opex.get());
            }
        } else {
            algorithm.getPrinter().print("");
        }

        return true;
    }
}
