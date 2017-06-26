package com.clientservertest.client;

import com.clientservertest.common.config.LoggerConfigurator;
import org.apache.log4j.Logger;

/**
 * Client main class.
 */
public class TestClient {
    private static final Logger LOG = Logger.getLogger(TestClient.class);

    /**
     * Main method of a test client application class.
     *
     * @param args Client startup parameters.
     *             Server hostname and port number could be specified, in other case - default values will be used.
     */
    @SuppressWarnings("checkstyle:magicnumber")
    public static void main(String[] args) {
        LoggerConfigurator.configure("client.log");


        int serverPort = 2323;
        String serverHost = "localhost";

        if (args.length == 2) {
            serverHost = args[0];

            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (Exception e) {
                LOG.error("Failed to start server: specified port is not a number");
                return;
            }
        }
        LOG.info(String.format("Calling %s:%d", serverHost, serverPort));

        Client client = new Client(serverHost, serverPort);

        for (int i = 0; i < 10; i++) {
            new Thread(new Caller(client)).start();
        }
    }
}
