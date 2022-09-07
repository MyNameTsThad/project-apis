package com.thaddev.projectapis.mapsystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class POI {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private int buildingId;
    private int level;
    private POITypes type;

    private double x;
    private double y;

    public POI(String name, int buildingId, double x, double y, POITypes type) {
        this.name = name;
        this.buildingId = buildingId;
        this.x = x;
        this.y = y;
    }

    public POI() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public POITypes getType() {
        return type;
    }

    public void setType(POITypes type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        POI poi = (POI) o;
        return id == poi.id && buildingId == poi.buildingId && Double.compare(poi.x, x) == 0 && Double.compare(poi.y, y) == 0 && Objects.equals(name, poi.name) && type == poi.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, buildingId, type, x, y);
    }

    @Override
    public String toString() {
        return "POI{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", buildingId=" + buildingId +
            ", type=" + type +
            ", x=" + x +
            ", y=" + y +
            '}';
    }
}

enum POITypes {
    STAIRS,
    WATER_DISPENER,
    TRASH_CAN,
    RESTROOM_MEN,
    RESTROOM_WOMEN,
    OTHER;

    public static POITypes getFromID(int id) {
        return POITypes.values()[id];
    }
}
