package com.clientservertest.server.workers;

import com.clientservertest.common.CommandRequest;
import com.clientservertest.common.CommandResponse;
import com.clientservertest.server.Services;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Client worker class. Handles client connection.
 */
public class ClientWorker implements Runnable {
    private static final Logger LOG = Logger.getLogger(ClientWorker.class);

    private Socket clientSocket;
    private Services services;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private ExecutorService executor;


    public ClientWorker(Services services, Socket clientSocket) {
        this.services = services;
        this.clientSocket = clientSocket;
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            LOG.error("Could not construct client worker!", e);
        }

        executor = Executors.newCachedThreadPool();

    }

    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    public void run() {
        try {
            LOG.info("Waiting for requests.");

            while (clientSocket.isConnected()) {
                try {
                    CommandRequest commandRequest = (CommandRequest) objectInputStream.readObject();
                    executor.execute(new RequestWorker(this, commandRequest, services));
                } catch (IOException | ClassNotFoundException e) {
                    LOG.error("Could not read object from socket!", e);
                    return;
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in Thread Run.", e);
        } finally {
            if (executor != null) {
                executor.shutdown();
            }
        }
    }

    /**
     * Write response to the clients output stream.
     *
     * @param commandResponse Result of command execution
     */
    public synchronized void write(CommandResponse commandResponse) {
        try {
            objectOutputStream.writeObject(commandResponse);
            objectOutputStream.flush();
        } catch (IOException e) {
            LOG.error("Could not write response to client!", e);
        }
    }
}
