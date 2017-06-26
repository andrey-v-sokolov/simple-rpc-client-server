package com.clientservertest.common.exceptions;

/**
 * Exception thrown when parameters type are wrong.
 */
public class WrongParametersTypeException extends Exception {
    public WrongParametersTypeException(String message) {
        super(message);
    }
}
