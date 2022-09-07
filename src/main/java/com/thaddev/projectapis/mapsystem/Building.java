package com.thaddev.projectapis.mapsystem;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private int height;

    private String name;
    private BuildingTypes type;

    @OneToMany(cascade = CascadeType.PERSIST)
    private List<POI> pois;

    public Building(double startX, double startY, double endX, double endY, String name, BuildingTypes type) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.name = name;
        this.type = type;
    }

    public Building() {
    }

    public int getId() {
        return id;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getStartY() {
        return startY;
    }

    public void setStartY(double startY) {
        this.startY = startY;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double getEndY() {
        return endY;
    }

    public void setEndY(double endY) {
        this.endY = endY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BuildingTypes getType() {
        return type;
    }

    public void setType(BuildingTypes type) {
        this.type = type;
    }

    public List<POI> getPois() {
        return pois;
    }

    public void addPoi(POI poi) {
        if (this.isInside(poi.getX(), poi.getY())) {
            poi.setBuildingId(id);
            this.pois.add(poi);
        }
    }

    public void removePoi(POI poi) {
        this.pois.remove(poi);
    }

    public void removePoi(int poi) {
        this.pois.remove(poi);
    }

    public void setPois(List<POI> pois) {
        for (POI poi : pois) {
            poi.setBuildingId(id);
        }
        this.pois = pois;
    }

    public boolean isInside(double x, double y) {
        return x >= startX && x <= endX && y >= startY && y <= endY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return id == building.id && Double.compare(building.startX, startX) == 0 && Double.compare(building.startY, startY) == 0 && Double.compare(building.endX, endX) == 0 && Double.compare(building.endY, endY) == 0 && Objects.equals(name, building.name) && Objects.equals(pois, building.pois);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startX, startY, endX, endY, name, pois);
    }

    @Override
    public String toString() {
        return
            "Building{" +
                "id=" + id +
                ", startX=" + startX +
                ", startY=" + startY +
                ", endX=" + endX +
                ", endY=" + endY +
                ", name='" + name + '\'' +
                ", pois=" + pois +
                '}';
    }
}

enum BuildingTypes {
    DORMITORY_MEN,
    DORMITORY_WOMEN,
    SCHOOL_BUILDING,
    CANTEEN,
    GYM,
    FIELD,
    NURSING
}
