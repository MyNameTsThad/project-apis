package com.thaddev.projectapis.mapsystem;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Map;
import java.util.Objects;

@Entity
public class POI {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private int level;
    private POITypes type;

    @ElementCollection
    private Map<POI, Integer> distances;

    private double x;
    private double y;

    public POI(String name, double x, double y, POITypes type) {
        this.name = name;
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

    public Map<POI, Integer> getDistances() {
        return distances;
    }

    public void setDistances(Map<POI, Integer> distances) {
        this.distances = distances;
    }

    public void addDistance(POI poi, int distance) {
        distances.put(poi, distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        POI poi = (POI) o;
        return id == poi.id && level == poi.level && Double.compare(poi.x, x) == 0 && Double.compare(poi.y, y) == 0 && Objects.equals(name, poi.name) && type == poi.type && Objects.equals(distances, poi.distances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level, type, x, y, distances);
    }

    @Override
    public String toString() {
        return "POI{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", level=" + level +
            ", type=" + type +
            ", distances=" + distances +
            ", x=" + x +
            ", y=" + y +
            '}';
    }
}

enum POITypes {
    WATER_DISPENER,
    TRASH_CAN,
    RESTROOM_MEN,
    RESTROOM_WOMEN,
    WAYPOINT;

    public static POITypes getFromID(int id) {
        return POITypes.values()[id];
    }
}
