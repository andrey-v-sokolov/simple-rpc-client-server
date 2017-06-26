package com.clientservertest.client;

import org.apache.log4j.Logger;

/**
 * Class representing caller which calls remote service methods on behalf of a client.
 */
public class Caller implements Runnable {
    private static final Logger LOG = Logger.getLogger(Caller.class);

    private Client client;

    Caller(Client client) {
        this.client = client;
    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            client.callRemote("service1", "sleep", new Object[]{1000L});
            LOG.info("Current Date is: " + client.callRemote("service1", "getCurrentDate", new Object[]{}));
        }
    }
}
