package com.clientservertest.common.exceptions;

/**
 * Exception thrown when specified method not found.
 */
public class NoMethodException extends Exception {
    public NoMethodException(String message) {
        super(message);
    }
}
