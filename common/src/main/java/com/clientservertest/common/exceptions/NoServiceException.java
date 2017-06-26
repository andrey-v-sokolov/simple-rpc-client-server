package com.clientservertest.common.exceptions;

/**
 * Exception thrown when specified service not found.
 */
public class NoServiceException extends Exception {
    public NoServiceException(String message) {
        super(message);
    }
}
