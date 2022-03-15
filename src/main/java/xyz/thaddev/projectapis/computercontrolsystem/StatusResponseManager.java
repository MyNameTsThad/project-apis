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
        setControllingTimerChanged(false);
        setExecuteCommand(false);
        setTimerLifeCycleChanged(false);
    }

    public StatusResponse getResponse() {
        genId();
        return response;
    }

    public boolean isControllingTimerChanged() {
        return isControllingTimerChanged;
    }

    public void setControllingTimerChanged(boolean controllingTimerChanged) {
        isControllingTimerChanged = controllingTimerChanged;
        setResponse();
    }

    public boolean isExecuteCommand() {
        return executeCommand;
    }

    public void setExecuteCommand(boolean executeCommand) {
        this.executeCommand = executeCommand;
        setResponse();
    }

    public boolean isTimerLifeCycleChanged() {
        return isTimerLifeCycleChanged;
    }

    public void setTimerLifeCycleChanged(boolean timerLifeCycleChanged) {
        isTimerLifeCycleChanged = timerLifeCycleChanged;
        setResponse();
    }

    void setResponse(){
        String sb = (isControllingTimerChanged ? "1-" : "0-") +
                (executeCommand ? "1-" : "0-") +
                (isTimerLifeCycleChanged ? "1" : "0");
        response.setStatus(sb);
    }

    public void genId(){
        response.setId((short) new Random().nextInt(Short.MAX_VALUE));
    }
}
