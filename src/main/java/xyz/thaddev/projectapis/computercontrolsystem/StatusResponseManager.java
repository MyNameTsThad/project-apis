package xyz.thaddev.projectapis.computercontrolsystem;

import xyz.thaddev.projectapis.ProjectApisApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StatusResponseManager {
    private StatusResponse response;

    private final Map<String, Long> connectedDevices = new HashMap<>();

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

    public void connectedDevicesPing(String ip) {
        connectedDevices.put(ip, System.currentTimeMillis());
        eliminateDevices();
    }

    private void eliminateDevices() {
        //loop through the map, and compare the time with the current time. If the time is greater than 4 seconds, remove the device.
        List<String> toRemove = new ArrayList<>();
        for (Map.Entry<String, Long> entry : connectedDevices.entrySet()) {
            if (System.currentTimeMillis() - entry.getValue() > 4000) {
                toRemove.add(entry.getKey());
            }
        }
        for (String s : toRemove) {
            connectedDevices.remove(s);
        }
    }

    public int getConnectedDevicesSize() {
        return connectedDevices.size();
    }

    public void clear() {
        isControllingTimerChanged = false;
        executeCommand = false;
        isTimerLifeCycleChanged = false;
        response = new StatusResponse();
        setResponse();
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

    void setResponse() {
        String sb = (isControllingTimerChanged ? "1-" : "0-") +
                (executeCommand ? "1-" : "0-") +
                (isTimerLifeCycleChanged ? "1" : "0");
        ProjectApisApplication.instance.logger.info("Set response to: " + sb);
        response.setStatus(sb);
    }

    public void genId() {
        response.setId((short) new Random().nextInt(Short.MAX_VALUE));
    }
}
