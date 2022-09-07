package com.thaddev.projectapis.chatthreadsystem.exceptions;

public class ThreadNotFoundException extends RuntimeException {
    public ThreadNotFoundException(long id) {
        super("Thread with id " + id + " not found");
    }
}
