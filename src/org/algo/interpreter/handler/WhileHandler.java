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

public class WhileHandler implements Handler {

    @Override
    public boolean handleEvent(Algorithm algorithm, int line, String fullLine) {
        String condition = fullLine.substring("Tant_que".length() + 1);
        final AtomicReference<String> opex = new AtomicReference<>(condition);
        algorithm.getVariables().forEach((key, value) -> opex.set(opex.get().replaceAll(key, String.valueOf(value.getValue()))));

        if (OpexM.isValidOperation("if(" + opex.get() + "){}")) {
            boolean valid = checkBoolean(opex.get());
            int endLine = getEndLine(algorithm, line);

            if (algorithm.getSkipTo() < endLine)
                algorithm.setSkipTo(endLine);

            if (valid) {
                Map<Integer, String> whileLine = new TreeMap<>();
                int index = 1;
                int skip = 0;
                for (int i = line + 1; i < endLine; i++) {
                    String algoLine = algorithm.getLines().get(i);
                    if(skip > i) continue;

                    algoLine = fullTrim(algoLine);

                    if(algoLine.split(" ")[0].equalsIgnoreCase("tant_que")) {
                        skip = getEndLine(algorithm, i) + 1;
                    }

                    if (algoLine.isEmpty())
                        continue;

                    whileLine.put(index++, algorithm.getLines().get(i));
                }

                while (checkBoolean(opex.get())) {
                    whileLine.forEach((i, currentWLine) -> {
                        currentWLine = fullTrim(currentWLine);
                        Interpreter.eval(new AtomicReference<>(State.CORE), currentWLine, i + line, algorithm);
                    });

                    opex.set(condition);
                    algorithm.getVariables().forEach((key, value) -> opex.set(opex.get().replaceAll(key, String.valueOf(value.getValue()))));
                }


            }
        } else {
            InterpreterException.invokeNew("Ligne " + line + " : condition non valide, il se peu que des variables soient inconnues", algorithm.getPrinter());
        }
        return true;
    }

    private int getEndLine(Algorithm algorithm, int line) {
        AtomicInteger view = new AtomicInteger(0);
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger endLine = new AtomicInteger(-1);

        algorithm.getLines().forEach((i, currentLine) -> {
            if (i <= line) return;

            currentLine = fullTrim(currentLine);

            if (currentLine.split(" ")[0].equalsIgnoreCase("tant_que")) {
                count.incrementAndGet();
            }

            if (currentLine.equalsIgnoreCase("Fin tant que") && endLine.get() == -1 && view.incrementAndGet() > count.get() ) {
                endLine.set(i);
            }
        });

        return endLine.get();
    }

}
