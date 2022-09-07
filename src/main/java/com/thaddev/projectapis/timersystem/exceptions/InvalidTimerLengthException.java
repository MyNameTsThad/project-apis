package com.thaddev.projectapis.timersystem.exceptions;

public class InvalidTimerLengthException extends RuntimeException {
    public InvalidTimerLengthException(long id, long length) {
        super("The Timer's Length time of " + length + " is Less than 1!");
    }
}
