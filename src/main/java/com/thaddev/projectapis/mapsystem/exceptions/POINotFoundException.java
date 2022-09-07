package com.thaddev.projectapis.mapsystem.exceptions;

public class POINotFoundException extends RuntimeException {
    public POINotFoundException(long id) {
        super("POI with id: " + id + " not found!");
    }
}
