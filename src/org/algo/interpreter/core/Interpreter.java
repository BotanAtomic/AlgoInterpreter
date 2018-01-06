package org.algo.interpreter.core;

import org.algo.interpreter.constant.State;
import org.algo.interpreter.handler.*;
import org.algo.interpreter.printer.DefaultPrinter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    }

    private final Algorithm algorithm;

    private State state = State.HEADER;

    public Interpreter(List<String> lines) {
        this.algorithm = new Algorithm(new DefaultPrinter());

        AtomicInteger lineCount = new AtomicInteger(0);
        lines.forEach(line -> {
            line = fullTrim(line);
            lineCount.incrementAndGet();

            if (fullTrim(line).isEmpty() || line.startsWith(COMMENT_LINE)) return;
            String[] arguments = line.split(" ");

            switch (state) {
                case HEADER:
                    handlers.get("header").handleEvent(algorithm, lineCount.get(), line);
                    this.state = State.VARIABLE_HEADER;
                    return;
                case VARIABLE_HEADER:
                    handlers.get("variable_header").handleEvent(algorithm, lineCount.get(), line);
                    this.state = State.VARIABLES;
                    return;
                case VARIABLES:
                    if (handlers.get("variables").handleEvent(algorithm, lineCount.get(), line))
                        this.state = State.CORE;
                    return;
                case CORE:
                    String qualifier = arguments[0].toLowerCase();
                    if (!qualifier.equalsIgnoreCase("fin")) {
                        Handler handler = handlers.getOrDefault(qualifier, new DefaultHandler());
                        handler.handleEvent(algorithm, lineCount.get(), line);
                    } else {
                        algorithm.getPrinter().print("\nFin de l'algoritme " + algorithm.getName());
                    }
            }
        });

    }

}
