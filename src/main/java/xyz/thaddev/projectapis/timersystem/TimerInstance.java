package xyz.thaddev.projectapis.timersystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import java.util.Objects;

@Entity
public class TimerInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int parentTimerId;
    private long startTime;
    private long timeLeft;
    private boolean isPaused;

    //special purposes
    private boolean isComputerControl;

    public TimerInstance() {
    }

    public TimerInstance(Timer parentTimer, boolean isComputerControl) {
        this.parentTimerId = parentTimer.getId();
        this.startTime = new Date().getTime();
        this.timeLeft = parentTimer.getLengthTime();
        this.isComputerControl = isComputerControl;
        this.isPaused = false;
    }

    public TimerInstance(Timer parentTimer, boolean isComputerControl, boolean isPaused) {
        this.parentTimerId = parentTimer.getId();
        this.startTime = new Date().getTime();
        this.timeLeft = parentTimer.getLengthTime();
        this.isComputerControl = isComputerControl;
        this.isPaused = isPaused;
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

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(long timeLeft) {
        this.timeLeft = timeLeft;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimerInstance that = (TimerInstance) o;
        return id == that.id && parentTimerId == that.parentTimerId && startTime == that.startTime && timeLeft == that.timeLeft && isPaused == that.isPaused && isComputerControl == that.isComputerControl;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentTimerId, startTime, timeLeft, isPaused, isComputerControl);
    }

    @Override
    public String toString() {
        return "TimerInstance{" +
                "id=" + id +
                ", parentTimerId=" + parentTimerId +
                ", startTime=" + startTime +
                ", timeLeft=" + timeLeft +
                ", isPaused=" + isPaused +
                ", isComputerControl=" + isComputerControl +
                '}';
    }
}
