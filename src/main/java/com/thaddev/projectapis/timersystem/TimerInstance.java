package com.thaddev.projectapis.timersystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class TimerInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int parentTimerId;
    private long startTime;
    private long timePaused;
    private long endTime;
    private boolean isPaused;

    //special purposes
    private boolean isComputerControl;

    public TimerInstance() {
    }

    public TimerInstance(Timer parentTimer, boolean isComputerControl) {
        this.parentTimerId = parentTimer.getId();
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + parentTimer.getLengthTime();
        this.timePaused = 0;
        this.isComputerControl = isComputerControl;
        this.isPaused = false;
    }

    public TimerInstance(Timer parentTimer, boolean isComputerControl, boolean isPaused) {
        this.parentTimerId = parentTimer.getId();
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + parentTimer.getLengthTime();
        this.timePaused = 0;
        this.isComputerControl = isComputerControl;
        this.isPaused = isPaused;
    }

    //INTERNAL USE ONLY
    public TimerInstance(int parentTimerId, long startTime, long timePaused, long endTime, boolean isPaused, boolean isComputerControl) {
        this.parentTimerId = parentTimerId;
        this.startTime = startTime;
        this.timePaused = timePaused;
        this.endTime = endTime;
        this.isPaused = isPaused;
        this.isComputerControl = isComputerControl;
    }

    public TimerInstance(int parentTimerId, long startTime, long timeLeft, boolean isPaused, boolean isComputerControl) {
        this.parentTimerId = parentTimerId;
        this.startTime = startTime;
        this.timePaused = 0;
        this.endTime = startTime + timeLeft;
        this.isPaused = isPaused;
        this.isComputerControl = isComputerControl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentTimerId() {
        return parentTimerId;
    }

    public void setParentTimerId(int parentTimerId) {
        this.parentTimerId = parentTimerId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isComputerControl() {
        return isComputerControl;
    }

    public void setComputerControl(boolean computerControl) {
        isComputerControl = computerControl;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getTimePaused() {
        return timePaused;
    }

    public void setTimePaused(long timePaused) {
        this.timePaused = timePaused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimerInstance that = (TimerInstance) o;
        return id == that.id && parentTimerId == that.parentTimerId && startTime == that.startTime && timePaused == that.timePaused && endTime == that.endTime && isPaused == that.isPaused && isComputerControl == that.isComputerControl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentTimerId, startTime, timePaused, endTime, isPaused, isComputerControl);
    }

    @Override
    public String toString() {
        return "TimerInstance{" +
                "id=" + id +
                ", parentTimerId=" + parentTimerId +
                ", startTime=" + startTime +
                ", timePaused=" + timePaused +
                ", endTime=" + endTime +
                ", isPaused=" + isPaused +
                ", isComputerControl=" + isComputerControl +
                '}';
    }
}
