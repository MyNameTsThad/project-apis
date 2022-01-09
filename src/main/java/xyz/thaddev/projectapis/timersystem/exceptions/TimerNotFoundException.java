package xyz.thaddev.projectapis.timersystem.exceptions;

public class TimerNotFoundException extends RuntimeException{
    public TimerNotFoundException(long id, boolean isInstance) {
        super("Cannot find " + (isInstance ? "TimerInstance" : "Timer") + " (" + id + ")");
    }
}
