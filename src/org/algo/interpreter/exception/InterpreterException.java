package org.algo.interpreter.exception;

import org.algo.interpreter.printer.Printer;

public class InterpreterException {

    public static void invokeNew(String message, Printer printer) {
        printer.printError(message);
        System.exit(0);
    }

}
