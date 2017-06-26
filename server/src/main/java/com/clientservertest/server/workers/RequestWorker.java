package com.clientservertest.server.workers;

import com.clientservertest.common.CommandRequest;
import com.clientservertest.common.CommandResponse;
import com.clientservertest.server.Services;
import org.apache.log4j.Logger;

/**
 * Request worker class. Handles client command execution.
 */
public class RequestWorker implements Runnable {
    private static final Logger LOG = Logger.getLogger(RequestWorker.class);

    private CommandRequest commandRequest;
    private Services services;
    private ClientWorker clientWorker;

    public RequestWorker(ClientWorker clientWorker, CommandRequest commandRequest, Services services) {
        this.commandRequest = commandRequest;
        this.services = services;
        this.clientWorker = clientWorker;
    }

    @Override
    public void run() {
        CommandResponse commandResponse = new CommandResponse();
        commandResponse.setId(commandRequest.getId());

        LOG.info(String.format("Processing: %s", commandRequest.toString()));

        Object call = null;

        try {
            call = services.call(commandRequest.getServiceName(), commandRequest.getMethodName(), commandRequest.getParams());
        } catch (Exception e) {
            LOG.info(String.format("CommandRequest %d caused exception: %s.", commandRequest.getId(), e.getMessage()));
            commandResponse.setException(e);
        }

        commandResponse.setResult(call);

        LOG.info(String.format("CommandRequest %d processed. ", commandRequest.getId()));

        clientWorker.write(commandResponse);

        LOG.info(commandResponse.toString() + " sent to client.");
    }
}
