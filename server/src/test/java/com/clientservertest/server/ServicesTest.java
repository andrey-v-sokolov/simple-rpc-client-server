package com.clientservertest.server;

import com.clientservertest.common.exceptions.NoMethodException;
import com.clientservertest.common.exceptions.NoServiceException;
import com.clientservertest.common.exceptions.WrongParameterCountException;
import com.clientservertest.common.exceptions.WrongParametersTypeException;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;

@RunWith(JUnit4.class)
public class ServicesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private Services testServices = new Services();

    @Before
    public void init() {
        BasicConfigurator.configure();
    }

    @Test
    public void callNoServiceException() throws Exception {
        thrown.expect(NoServiceException.class);
        Object[] params = new Object[0];
        testServices.call("nonExistentService", "nonExistentMethod", params);
    }

    @Test
    public void callNoMethodException() throws Exception {
        thrown.expect(NoMethodException.class);
        Object[] params = new Object[0];
        testServices.call("service1", "nonExistentMethod", params);
    }

    @Test
    public void callWrongParameterCountException() throws Exception {
        thrown.expect(WrongParameterCountException.class);
        Object[] params = new Object[1];
        testServices.call("service1", "getCurrentDate", params);
    }

    @Test
    public void callWrongParametersTypeException() throws Exception {
        thrown.expect(WrongParametersTypeException.class);
        Object[] params = new Object[1];
        params[0] = "String instead of a Long";
        testServices.call("service1", "sleep", params);
    }

    @Test
    public void callVoid() throws Exception {
        Object[] params = new Object[1];
        params[0] = 10L;
        Object result = testServices.call("service1", "sleep", params);

        assert result != null;
    }

    @Test
    public void callDate() throws Exception {
        Object[] params = new Object[0];
        Object result = testServices.call("service1", "getCurrentDate", params);

        assert result.getClass().equals(Date.class);
    }
}
