package com.careem.annotation.processing.converter.util;

/**
 * Basic utility class to generate expressions
 */
public class BasicExpressionUtil {
    private BasicExpressionUtil() {
    }

    public static String nullCheck(String x, String y, String defaultValue) {
        return x + " != null ? (" + y + ") : " + defaultValue;
    }

    public static String methodCall(String methodName, String arg1) {
        return methodName + "(" + arg1 + ")";
    }

    public static String methodCall(String methodName, String arg1, String arg2) {
        return methodName + "(" + arg1 + ", " + arg2 + ")";
    }
}
