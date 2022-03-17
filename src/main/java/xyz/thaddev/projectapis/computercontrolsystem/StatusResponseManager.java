package xyz.thaddev.projectapis.computercontrolsystem;

import xyz.thaddev.projectapis.ProjectApisApplication;

import java.util.Random;

public class StatusResponseManager {
    private StatusResponse response;

    private boolean isControllingTimerChanged;
    private boolean executeCommand;
    private boolean isTimerLifeCycleChanged;

    public StatusResponseManager() {
        ProjectApisApplication.instance.setStatusResponseManager(this);
        response = new StatusResponse();
        isControllingTimerChanged = false;
        executeCommand = false;
        isTimerLifeCycleChanged = false;
    }

    public void clear() {
        isControllingTimerChanged = false;
        executeCommand = false;
        isTimerLifeCycleChanged = false;
        response = new StatusResponse();
    }

    public StatusResponse getResponse() {
        genId();
        return response;
    }

    public boolean isControllingTimerChanged() {
        return isControllingTimerChanged;
    }

    public void setControllingTimerChanged() {
        isControllingTimerChanged = true;
        ProjectApisApplication.instance.logger.info("Controlling timer changed");
        setResponse();
    }

    public boolean isExecuteCommand() {
        return executeCommand;
    }

    public void setExecuteCommand() {
        this.executeCommand = true;
        ProjectApisApplication.instance.logger.info("New command received");
        setResponse();
    }

    public boolean isTimerLifeCycleChanged() {
        return isTimerLifeCycleChanged;
    }

    public void setTimerLifeCycleChanged() {
        isTimerLifeCycleChanged = true;
        ProjectApisApplication.instance.logger.info("Timer life cycle changed");
        setResponse();
    }

    void setResponse(){
        String sb = (isControllingTimerChanged ? "1-" : "0-") +
                (executeCommand ? "1-" : "0-") +
                (isTimerLifeCycleChanged ? "1" : "0");
        ProjectApisApplication.instance.logger.info("Set response to: " + sb);
        response.setStatus(sb);
    }

    public void genId(){
        response.setId((short) new Random().nextInt(Short.MAX_VALUE));
    }
}
