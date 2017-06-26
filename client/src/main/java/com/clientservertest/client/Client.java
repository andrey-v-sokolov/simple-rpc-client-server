package com.clientservertest.client;

import com.clientservertest.common.CommandRequest;
import com.clientservertest.common.CommandResponse;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Client class represents a single client, dispatch client execution to a daemon and holds its connection to the server.
 */
public class Client {
    private static final Logger LOG = Logger.getLogger(Client.class);

    private Long id = 0L;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private ConcurrentHashMap<Long, CompletableFuture<CommandResponse>> callState = new ConcurrentHashMap<>();
    private Lock writeLock = new ReentrantLock();

    public Client(String serverHost, int serverPort) {

        try {
            socket = new Socket(serverHost, serverPort);
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            LOG.error("Could not construct client!", e);
        }

        daemonize(this::run);
    }

    /**
     * Method allows to call remote service methods.
     *
     * @param serviceName Name of a service to call
     * @param methodName  Name of service method to invoke
     * @param params      Parameters to call method with. Could not be primitive types
     * @return Result of remote method invocation
     */
    public Object callRemote(String serviceName, String methodName, Object[] params) {
        CommandRequest commandRequest = new CommandRequest(++id, serviceName, methodName, params);
        CommandResponse commandResponse;

        try {
            callState.put(commandRequest.getId(), new CompletableFuture<>());

            writeLock.lock();
            objectOutputStream.writeObject(commandRequest);
            writeLock.unlock();

            LOG.info(commandRequest.toString() + " sent.");

            Future<CommandResponse> responseCompletableFuture = callState.get(commandRequest.getId());

            commandResponse = responseCompletableFuture.get();
            if (commandResponse.getException() != null) {
                throw commandResponse.getException();
            } else {
                return commandResponse.getResult();
            }
        } catch (Exception e) {
            LOG.error("An error occurred at remote call attempt!", e);
        }
        return null;
    }

    private void daemonize(Runnable target) {
        Thread daemon = new Thread(target);
        daemon.setDaemon(true);
        daemon.start();
    }

    private void run() {
        try {
            while (socket.isConnected()) {
                CommandResponse commandResponse = (CommandResponse) objectInputStream.readObject();

                if (commandResponse.getResult() == null) {
                    LOG.info(String.format("%s received.", commandResponse.toString()));
                } else {
                    LOG.info(String.format("%s received: %s.", commandResponse.toString(), commandResponse.getResult().toString()));
                }

                CompletableFuture<CommandResponse> responseCompletableFuture = callState.get(commandResponse.getId());
                responseCompletableFuture.complete(commandResponse);
                callState.put(commandResponse.getId(), responseCompletableFuture);
            }
        } catch (Exception e) {
            LOG.error("An error occurred while client running!", e);
        }
    }
}
