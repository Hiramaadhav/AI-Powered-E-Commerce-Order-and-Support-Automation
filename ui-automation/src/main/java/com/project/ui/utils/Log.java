package com.project.ui.utils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Log {
    private static Logger logger;

    private Log() {
		// private constructor to prevent instantiation
    }

    public static Logger getLogger(Class<?> clazz) {
        logger = LogManager.getLogger(clazz);
        return logger;
    }

    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }

    public static void debug(Class<?> clazz, String message) {
        getLogger(clazz).debug(message);
    }

    public static void error(Class<?> clazz, String message) {
        getLogger(clazz).error(message);
    }

    public static void error(Class<?> clazz, String message, Throwable t) {
        getLogger(clazz).error(message, t);
    }
}
