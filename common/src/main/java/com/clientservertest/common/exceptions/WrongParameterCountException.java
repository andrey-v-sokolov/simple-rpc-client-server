package com.clientservertest.common.exceptions;

/**
 * Exception thrown when parameters number is wrong.
 */
public class WrongParameterCountException extends Exception {
    public WrongParameterCountException(String message) {
        super(message);
    }
}
