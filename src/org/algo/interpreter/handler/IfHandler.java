package org.algo.interpreter.handler;

import org.algo.interpreter.constant.State;
import org.algo.interpreter.core.Algorithm;
import org.algo.interpreter.core.Interpreter;
import org.algo.interpreter.exception.InterpreterException;
import org.algo.interpreter.utils.OpexM;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.algo.interpreter.utils.OpexM.checkBoolean;
import static org.algo.interpreter.utils.StringUtils.fullTrim;

public class IfHandler implements Handler {

    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {

        String condition = fullLine.substring("Si".length() + 1);
        final AtomicReference<String> opex = new AtomicReference<>(condition);
        algorithm.getVariables().forEach((key, value) -> opex.set(opex.get().replaceAll(key, String.valueOf(value.getValue()))));

        if (OpexM.isValidOperation("if(" + opex.get() + "){}")) {
            boolean valid = checkBoolean(opex.get());
            int elseLine = getElseLine(algorithm, line);
            int endLine = getEndLine(algorithm, line);

            Map<Integer, String> ifValid = new TreeMap<>();
            Map<Integer, String> ifNotValid = new TreeMap<>();

            int index = 1;
            for (int i = line + 1; i < elseLine; i++) {
                String algoLine = algorithm.getLines().get(i);

                if (fullTrim(algoLine).isEmpty())
                    continue;

                ifValid.put(index++, algorithm.getLines().get(i));
            }

            index = 1;
            for (int i = elseLine + 1; i < endLine; i++) {
                String algoLine = algorithm.getLines().get(i);

                if (fullTrim(algoLine).isEmpty())
                    continue;

                ifNotValid.put(index++, algorithm.getLines().get(i));
            }

            algorithm.setSkipTo(endLine);

            if (valid) {
                ifValid.forEach((i,currentWLine) -> {
                    currentWLine = fullTrim(currentWLine);
                    Interpreter.eval(new AtomicReference<>(State.CORE), currentWLine, i + line, algorithm);
                });
            } else {
                ifNotValid.forEach((i,currentWLine) -> {
                    currentWLine = fullTrim(currentWLine);
                    Interpreter.eval(new AtomicReference<>(State.CORE), currentWLine, i + line, algorithm);
                });
            }
        } else {
            InterpreterException.invokeNew("Ligne " + line + " : condition non valide, il se peu que des variables soient inconnues", algorithm.getPrinter());
        }
        return true;
    }

    private int getElseLine(Algorithm algorithm, int line) {
        AtomicInteger elseLine = new AtomicInteger(-1);

        algorithm.getLines().forEach((i, currentLine) -> {
            if (i < line) return;

            if (fullTrim(currentLine).equalsIgnoreCase("sinon") && elseLine.get() == -1) {
                elseLine.set(i);
            }
        });

        return elseLine.get();
    }

    private int getEndLine(Algorithm algorithm, int line) {
        AtomicInteger endLine = new AtomicInteger(-1);

        algorithm.getLines().forEach((i, currentLine) -> {
            if (i < line) return;

            if (fullTrim(currentLine).equalsIgnoreCase("finsi") && endLine.get() == -1) {
                endLine.set(i);
            }
        });

        return endLine.get();
    }

}
