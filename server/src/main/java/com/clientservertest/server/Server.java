package com.clientservertest.server;

import com.clientservertest.server.workers.ClientWorker;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class representing server with client listening socket and services instance.
 */
public class Server {
    private static final Logger LOG = Logger.getLogger(Server.class);

    private ServerSocket serverSocket;
    private Services services;
    private ExecutorService executor;

    public Server(Integer serverPort) {

        try {
            this.serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            LOG.error("Failed to start server!", e);
            return;
        }

        services = new Services();
        executor = Executors.newCachedThreadPool();

        start(this::run);
    }

    private void start(Runnable target) {
        Thread daemon = new Thread(target);
        daemon.start();
    }

    private void run() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    executor.execute(new ClientWorker(services, clientSocket));
                } catch (IOException e) {
                    LOG.error("Client accept failed!", e);
                }
            }
        } catch (Exception e) {
            LOG.error("An error occurred while server running!", e);
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }
}
