package org.algo.interpreter.printer;

public class DefaultPrinter implements Printer {

    @Override
    public void print(String line) {
        System.out.println(line);
    }

    @Override
    public void printError(String line) {
        System.err.println(line);
    }

}
