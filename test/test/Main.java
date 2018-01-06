package test;


import org.algo.interpreter.core.Interpreter;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        new Interpreter(Files.readAllLines(Paths.get("algo.txt")));
    }

}
