package com.thaddev.projectapis.mapsystem.exceptions;

import com.thaddev.projectapis.mapsystem.Building;
import com.thaddev.projectapis.mapsystem.POI;

public class POIPositionOutOfBoundsException extends RuntimeException {
    public POIPositionOutOfBoundsException(POI culprit, Building attempted) {
        super("POI out of bounds of building");
    }
}
