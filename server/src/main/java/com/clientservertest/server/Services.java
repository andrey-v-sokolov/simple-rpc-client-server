package com.clientservertest.server;

import com.clientservertest.common.exceptions.NoMethodException;
import com.clientservertest.common.exceptions.NoServiceException;
import com.clientservertest.common.exceptions.WrongParameterCountException;
import com.clientservertest.common.exceptions.WrongParametersTypeException;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Services class. Holds one instance of each service and represents an interface to call their methods.
 */
public class Services {
    private static final Logger LOG = Logger.getLogger(Services.class);

    private ConcurrentHashMap<String, Object> services = new ConcurrentHashMap<>();

    public Services() {

        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("server.properties")) {
            properties.load(inputStream);
        } catch (Exception e) {
            LOG.error("Failed to read properties! Make sure server.properties file exist and well formatted.", e);
            System.exit(-1);
        }

        properties.forEach((k, v) -> {
            try {
                ClassLoader classloader = ClassLoader.getSystemClassLoader();

                String serviceName = k.toString();
                Object instance = classloader.loadClass(v.toString()).newInstance();
                services.put(serviceName, instance);

                LOG.info("Service registered: " + k);
            } catch (Exception e) {
                LOG.error("An error occurred while service " + k + " registration attempt!", e);
            }
        });
    }

    /**
     * Method allows to call specified method of specified service using specified parameters.
     *
     * @param serviceName Name of a service to call
     * @param methodName  Name of service method to invoke
     * @param params      Parameters to call method with. Could not be primitive types
     * @return Result of method invocation
     * @throws NoServiceException           will be thrown if specified method is not registered
     * @throws NoMethodException            will be thrown if service doesn't have specified method
     * @throws WrongParameterCountException will be thrown if wrong number of parameters were specified
     * @throws WrongParametersTypeException will be thrown if wrong types of parameters were specified
     */
    public Object call(String serviceName, String methodName, Object[] params)
            throws NoServiceException, NoMethodException, WrongParameterCountException, WrongParametersTypeException {

        //Check if service registered.
        if (!services.containsKey(serviceName)) {
            throw new NoServiceException("No service found: " + serviceName);
        }

        Object serviceObject = services.get(serviceName);
        Class serviceObjectClass = serviceObject.getClass();

        Integer paramsCount = params != null ? params.length : 0;


        //Usually, client requests are correct.
        Method method = null;
        try {
            Class<?>[] paramTypes = getParamTypes(params);
            method = serviceObjectClass.getDeclaredMethod(methodName, paramTypes);
        } catch (Exception e) {
            //If not, we need to find a cause.
            determineCause(methodName, serviceObjectClass, paramsCount);
        }

        Object invocationResult = null;

        try {
            invocationResult = method.invoke(serviceObject, params);
            //Special handling of the void methods.
            if (Void.TYPE.equals(method.getReturnType())) {
                invocationResult = Void.TYPE;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            LOG.error("Method invocation failed!", e);
        }

        return invocationResult;
    }

    private void determineCause(String methodName, Class serviceObjectClass, int paramsCount)
            throws NoMethodException, WrongParameterCountException, WrongParametersTypeException {

        //Try to find by name.
        List<Method> coincidingByName = Arrays.stream(serviceObjectClass.getDeclaredMethods())
                .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());

        if (coincidingByName.isEmpty()) {
            throw new NoMethodException("No method found: " + methodName);
        }

        //It could be overloaded methods. Filtering by parameters count, and checks if any methods correspond.
        List<Method> coincidingByParameterCount = coincidingByName.stream()
                .filter(method -> method.getParameterCount() == paramsCount).collect(Collectors.toList());

        if (coincidingByParameterCount.isEmpty()) {
            throw new WrongParameterCountException("Wrong parameters count!");
        }

        //Only one possible issue left.
        throw new WrongParametersTypeException("Wrong parameters!");
    }

    private Class<?>[] getParamTypes(Object[] params) {
        Class[] parameterTypes;

        if (params != null) {
            parameterTypes = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                parameterTypes[i] = params[i].getClass();
            }
            return parameterTypes;
        } else {
            parameterTypes = new Class[0];
            return parameterTypes;
        }
    }
}
