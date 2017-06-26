package com.clientservertest.common;

import java.io.Serializable;

/**
 * A command response to client with service call result.
 */
public class CommandResponse implements Serializable {
    private Long id;
    private Object result;
    private Exception exception;

    @Override
    public String toString() {
        return "CommandResponse{"
                + "id=" + id
                + ", result=" + result
                + ", exception=" + exception
                + '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
