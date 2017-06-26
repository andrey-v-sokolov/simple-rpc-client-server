package com.clientservertest.common;

import java.io.Serializable;
import java.util.Arrays;

/**
 * A command request to server to call a specified method of specified service with specified parameters. Be advised that primitive types could
 * not be passed as parameters.
 */
public class CommandRequest implements Serializable {

    private Long id;
    private String serviceName;
    private String methodName;
    private Object[] params;

    public CommandRequest(Long id, String serviceName, String methodName, Object[] params) {
        this.id = id;
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    public String toString() {
        return String.format("CommandRequest with id=%d to call serviceName=%s methodName=%s with params=%s",
                id, serviceName, methodName, Arrays.toString(params));
    }

    public Long getId() {
        return id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParams() {
        return params;
    }

}
