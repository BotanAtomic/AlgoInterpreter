package org.algo.interpreter.core;

import org.algo.interpreter.constant.State;
import org.algo.interpreter.handler.*;
import org.algo.interpreter.printer.DefaultPrinter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.algo.interpreter.constant.Constant.COMMENT_LINE;
import static org.algo.interpreter.utils.StringUtils.fullTrim;

public class Interpreter {
    private static Map<String, Handler> handlers = new HashMap<>();

    static {
        handlers.put("header", new HeaderHandler());
        handlers.put("variable_header", new VariableHeader());
        handlers.put("variables", new VariableInitialization());
        handlers.put("afficher", new PrintHandler());
        handlers.put("saisir", new InputHandler());
        handlers.put("tant_que", new WhileHandler());
        handlers.put("si", new IfHandler());

    }

    private final Algorithm algorithm;

    private State state = State.HEADER;

    public Interpreter(List<String> lines) {
        this.algorithm = new Algorithm(new DefaultPrinter());
        AtomicInteger lineCount = new AtomicInteger(0);

        lines.forEach(line -> this.algorithm.getLines().put(lineCount.incrementAndGet(), line));

        lineCount.set(0);
        lines.forEach(line -> {
            line = fullTrim(line);
            lineCount.incrementAndGet();

            if (fullTrim(line).isEmpty() || line.startsWith(COMMENT_LINE)) return;

            AtomicReference<State> atomicState = new AtomicReference<>(state);

            if (lineCount.get() > algorithm.getSkipTo())
                eval(atomicState, line, lineCount.get(), algorithm);

            this.state = atomicState.get();
        });

    }

    public static void eval(AtomicReference<State> state, String line, int lineCount, Algorithm algorithm) {
        String[] arguments = line.split(" ");

        switch (state.get()) {
            case HEADER:
                handlers.get("header").handleEvent(algorithm, lineCount, line);
                state.set(State.VARIABLE_HEADER);
                return;
            case VARIABLE_HEADER:
                handlers.get("variable_header").handleEvent(algorithm, lineCount, line);
                state.set(State.VARIABLES);
                return;
            case VARIABLES:
                if (handlers.get("variables").handleEvent(algorithm, lineCount, line))
                    state.set(State.CORE);
                return;
            case CORE:
                String qualifier = arguments[0].toLowerCase();
                if (!line.equalsIgnoreCase("fin")) {
                    Handler handler = handlers.getOrDefault(qualifier, new DefaultHandler());
                    handler.handleEvent(algorithm, lineCount, line);
                } else {
                    algorithm.getPrinter().print("\nFin de l'algoritme " + algorithm.getName());
                }
        }
    }

}
