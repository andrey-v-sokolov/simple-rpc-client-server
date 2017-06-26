package com.clientservertest.server.services;

import java.util.Date;

/**
 * Test service.
 */
public class Service1 {
    /**
     * Test method. Sets execution thread into sleep for specified time.
     *
     * @param millis Sleep time for thread in milliseconds
     * @throws InterruptedException if interrupted
     */
    public void sleep(Long millis) throws InterruptedException {
        Thread.sleep(millis);
    }

    /**
     * Test method.
     *
     * @return current date object.
     */
    public Date getCurrentDate() {
        return new Date();
    }
}
