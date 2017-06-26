package com.clientservertest.common.config;

import org.apache.log4j.*;

/**
 * Provides configuration for loggers.
 */
public class LoggerConfigurator {

    /**
     * Provide default configuration.
     */
    public static void configure() {
        configure(null);
    }

    /**
     * Configure logger to append to a specified file and console.
     *
     * @param fileName to append logs
     */
    public static void configure(String fileName) {
        PatternLayout layout = new PatternLayout();
        String conversionPattern = "[%p] %d %c %M - %m%n";
        layout.setConversionPattern(conversionPattern);

        DailyRollingFileAppender fileAppender = new DailyRollingFileAppender();

        fileName = fileName != null ? fileName : "out.log";

        fileAppender.setFile(fileName);
        fileAppender.setLayout(layout);
        fileAppender.activateOptions();

        ConsoleAppender consoleAppender = new ConsoleAppender();

        consoleAppender.setTarget(ConsoleAppender.SYSTEM_OUT);
        consoleAppender.setLayout(layout);
        consoleAppender.activateOptions();

        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.DEBUG);
        rootLogger.addAppender(fileAppender);
        rootLogger.addAppender(consoleAppender);
    }
}
