package org.algo.interpreter.type;

import org.algo.interpreter.core.Algorithm;
import org.algo.interpreter.exception.InterpreterException;
import org.algo.interpreter.object.Variable;
import org.algo.interpreter.utils.Calculator;

import java.util.concurrent.atomic.AtomicReference;

import static org.algo.interpreter.utils.StringUtils.removeAccent;

public class ParsableType {

    public static Object parseType(String type) {
        switch (removeAccent(type)) {
            case "entier":
                return 0;

            case "decimal":
            case "reels":
                return 0f;

            default:
                return new Object();
        }
    }

    public static Object parseType(String type, int arraySize) {
        switch (removeAccent(type)) {
            case "entier":
                return new Integer[arraySize];

            case "decimal":
            case "reels":
                return new Float[arraySize];

            default:
                return new Object[arraySize];
        }
    }

    public static void assignNewValue(Variable variable, int line, Algorithm algorithm, String parsableValue) {
        if (variable.getValue() instanceof Integer) {
            variable.setValue(parseNumberValue(variable, parsableValue, line, algorithm).intValue());
        } else if (variable.getValue() instanceof Float) {
            variable.setValue(parseNumberValue(variable, parsableValue, line, algorithm).floatValue());
        } else if (variable.getValue() != null) {

        }
    }

    private static Number parseNumberValue(Variable root, String parsableValue, int line, Algorithm algorithm) {
        Number result = 0;
        try {
            result = Integer.parseInt(parsableValue);
        } catch (NumberFormatException ignored) {
            Variable variable = algorithm.getVariables().get(parsableValue);

            if (variable != null) {
                if (Variable.isCompatible(root, variable)) {
                    result = (Number) variable.getValue();
                } else {
                    InterpreterException.invokeNew("Ligne " + line + " : variables incompatibles", algorithm.getPrinter());
                }
            } else {
                AtomicReference<String> opex = new AtomicReference<>(parsableValue);
                algorithm.getVariables().forEach((key, value) -> opex.set(opex.get().replaceAll(key, String.valueOf(value.getValue()))));

                if(Calculator.isValidOperation(opex.get())) {
                    result =  Calculator.result(opex.get());
                } else {
                    InterpreterException.invokeNew("Ligne " + line + " : ligne de calcul incorrect, il se peut que des variables soient inconnues", algorithm.getPrinter());
                }

            }
        }

        return result;
    }

}
