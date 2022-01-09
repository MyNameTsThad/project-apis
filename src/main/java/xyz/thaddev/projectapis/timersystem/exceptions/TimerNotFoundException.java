package xyz.thaddev.projectapis.timersystem.exceptions;

public class TimerNotFoundException extends RuntimeException{
    public TimerNotFoundException(long id) {
        super("Cannot find Timer/TimerInstance (" + id + ")");
    }
}
