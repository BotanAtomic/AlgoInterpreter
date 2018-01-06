package org.algo.interpreter.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static org.algo.interpreter.utils.StringUtils.fullTrim;

public class Calculator {
    public static final ScriptEngine javaScript = new ScriptEngineManager().getEngineByName("JavaScript");

    public static double result(String operation) {
        try {
            Object result = javaScript.eval(fullTrim(operation));

            if(result == null)
                return 0;

            if (result instanceof Double)
                return (double) result;
            else
                return (int) result;

        } catch (ScriptException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isValidOperation(String operation) {
        try {
            javaScript.eval(operation);
            return true;
        } catch (ScriptException e) {
            return false;
        }
    }

}
