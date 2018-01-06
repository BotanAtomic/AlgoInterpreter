package org.algo.interpreter.utils;

import sun.text.Normalizer;

import java.util.stream.IntStream;

public class StringUtils {

    public static String concatArray(String[] array, int index, String separator) {
        StringBuilder builder = new StringBuilder();

        IntStream.range(index, array.length).forEach(i -> builder.append(separator.concat(array[i])));

        return builder.toString().trim();
    }

    public static String fullTrim(String value) {
        int len = value.length();
        int st = 0;
        while ((st < len) && value.charAt(len - 1) == ' ') {
            len--;
        }
        return value.substring(0, len).trim();
    }

    public static String removeAccent(String source) {
        return Normalizer.normalize(source, java.text.Normalizer.Form.NFD,0).replaceAll("[\u0300-\u036F]", "");
    }

}
