package com.thaddev.projectapis.mapsystem.exceptions;

public class InvalidBuildingException extends RuntimeException {
    public InvalidBuildingException(long id) {
        super("Invalid Building! (" + id + ")");
    }
}
