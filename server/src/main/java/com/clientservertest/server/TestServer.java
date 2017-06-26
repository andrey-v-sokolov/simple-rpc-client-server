package com.clientservertest.server;

import com.clientservertest.common.config.LoggerConfigurator;
import org.apache.log4j.Logger;

/**
 * Server main class.
 */
public class TestServer {
    private static final Logger LOG = Logger.getLogger(TestServer.class);

    /**
     * Main method of a test server application.
     *
     * @param args Server startup parameters.
     *             Port number could be specified, in other case - default value will be used.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public static void main(String[] args) {
        LoggerConfigurator.configure("server.log");

        Integer serverPort = 2323;

        if (args.length == 1) {
            try {
                serverPort = Integer.parseInt(args[0]);
            } catch (Exception e) {
                LOG.error("Failed to start server: specified port could not be parsed!", e);
                return;
            }
        }

        LOG.info("Starting server on port " + serverPort);
        new Server(serverPort);
        LOG.info("Listening...");
    }
}
