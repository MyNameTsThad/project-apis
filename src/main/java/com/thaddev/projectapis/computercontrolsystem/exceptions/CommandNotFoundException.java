package com.thaddev.projectapis.computercontrolsystem.exceptions;

public class CommandNotFoundException extends RuntimeException {
    public CommandNotFoundException(long id) {
        super("Cannot find Command (" + id + ")");
    }
}
