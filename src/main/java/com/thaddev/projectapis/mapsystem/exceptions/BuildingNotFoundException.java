package com.thaddev.projectapis.mapsystem.exceptions;

public class BuildingNotFoundException extends RuntimeException {
    public BuildingNotFoundException(long id) {
        super("Building with id: " + id + " not found!");
    }
}
