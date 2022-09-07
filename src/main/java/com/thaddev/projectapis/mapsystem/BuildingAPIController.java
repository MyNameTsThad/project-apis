package com.thaddev.projectapis.mapsystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thaddev.projectapis.PermissionDeniedException;
import com.thaddev.projectapis.ProjectApisApplication;
import com.thaddev.projectapis.mapsystem.exceptions.BuildingNotFoundException;
import com.thaddev.projectapis.mapsystem.exceptions.InvalidBuildingException;
import com.thaddev.projectapis.mapsystem.exceptions.POINotFoundException;
import org.apache.commons.io.FileUtils;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class BuildingAPIController {
    private final BuildingRepostory buildingRepostory;
    private final POIRepostory poiRepostory;

    public BuildingAPIController(BuildingRepostory buildingRepostory, POIRepostory poiRepostory) {
        this.buildingRepostory = buildingRepostory;
        this.poiRepostory = poiRepostory;
        ProjectApisApplication.instance.setBuildingAPIController(this);
        new Thread("loadFromFile-mapssystem") {
            @Override
            public void run() {
                try {
                    readBuildingsFromFile();
                    readPOIsFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @GetMapping("/api-v1/locator/getbuildings")
    private List<Building> getAllBuildings() {
        return buildingRepostory.findAll();
    }

    @GetMapping("/api-v1/locator/getbuilding")
    private Building getBuilding(@RequestParam int id) {
        return buildingRepostory.findById(id)
            .orElseThrow(() -> new BuildingNotFoundException(id));
    }

    @PostMapping("/api-v1/locator/addbuilding")
    private Building addBuilding(@RequestBody Building newBuilding) {
        if (newBuilding.getName() != null && newBuilding.getName().length() > 0) {
            Building saved = buildingRepostory.save(newBuilding);
            ProjectApisApplication.instance.logger.info("Saved new Building: " + newBuilding.getName());
            new Thread("saveToFile") {
                @Override
                public void run() {
                    try {
                        saveBuildingsToFile();
                        savePOIsToFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            return saved;
        } else {
            throw new InvalidBuildingException(newBuilding.getId());
        }
    }

    @PatchMapping("/api-v1/locator/move")
    public Building moveBuilding(@RequestBody Pair<Pair<Double, Double>, Pair<Double, Double>> pos, @RequestParam int id) {
        return buildingRepostory.findById(id)
            .map(building -> {
                building.setStartX(pos.getFirst().getFirst());
                building.setStartY(pos.getFirst().getSecond());
                building.setEndX(pos.getSecond().getFirst());
                building.setEndY(pos.getSecond().getSecond());
                Building saved = buildingRepostory.save(building);
                new Thread("saveToFile") {
                    @Override
                    public void run() {
                        try {
                            saveBuildingsToFile();
                            savePOIsToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return saved;
            })
            .orElseThrow(() -> new BuildingNotFoundException(id));
    }

    @PatchMapping("/api-v1/locator/rename")
    public Building renameBuilding(@RequestBody String newName, @RequestParam int id) {
        return buildingRepostory.findById(id)
            .map(building -> {
                building.setName(newName);
                Building saved = buildingRepostory.save(building);
                new Thread("saveToFile") {
                    @Override
                    public void run() {
                        try {
                            saveBuildingsToFile();
                            savePOIsToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return saved;
            })
            .orElseThrow(() -> new BuildingNotFoundException(id));
    }

    @DeleteMapping("/api-v1/locator/deletebuilding")
    public void deleteBuilding(@RequestParam String authPassword, @RequestParam int id) {
        if (authPassword.equals(ProjectApisApplication.authPassword)) {
            if ((Object) buildingRepostory.findById(id) != Optional.empty()) {
                buildingRepostory.deleteById(id);
                ProjectApisApplication.instance.logger.info("Deleted Building by id: " + id);
                new Thread("saveToFile") {
                    @Override
                    public void run() {
                        try {
                            saveBuildingsToFile();
                            savePOIsToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } else {
                throw new BuildingNotFoundException(id);
            }
        } else {
            throw new PermissionDeniedException();
        }
    }

    @GetMapping("/api-v1/locator/getpois/{id}")
    private List<POI> getPOIsInBuilding(@PathVariable int id) {
        return getBuilding(id).getPois();
    }

    @GetMapping("/api-v1/locator/getpois")
    private List<POI> getAllPOIs() {
        return poiRepostory.findAll();
    }

    @GetMapping("/api-v1/locator/getpoi")
    private POI getPOIById(@RequestParam int id) {
        return poiRepostory.findById(id)
            .orElseThrow(() -> new POINotFoundException(id));
    }

    @PostMapping("/api-v1/locator/addpoi")
    public POI addPOI(@RequestBody POI toAdd) {
        List<Building> buildings = buildingRepostory.findAll();
        for (Building building : buildings) {
            if (building.isInside(toAdd.getX(), toAdd.getY())) {
                buildingRepostory.findById(building.getId())
                    .map(building1 -> {
                        building1.addPoi(toAdd);
                        Building saved = buildingRepostory.save(building1);
                        new Thread("saveToFile") {
                            @Override
                            public void run() {
                                try {
                                    saveBuildingsToFile();
                                    savePOIsToFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                        return saved;
                    })
                    .orElseThrow(() -> new BuildingNotFoundException(building.getId()));
            }
        }
        return poiRepostory.save(toAdd);
    }

    @PatchMapping("/api-v1/locator/movepoi")
    public POI movePOI(@RequestBody Pair<Double, Double> pos, @RequestParam int id) {
        return poiRepostory.findById(id)
            .map(poi -> {
                poi.setX(pos.getFirst());
                poi.setY(pos.getSecond());
                POI saved = poiRepostory.save(poi);
                new Thread("saveToFile") {
                    @Override
                    public void run() {
                        try {
                            saveBuildingsToFile();
                            savePOIsToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return saved;
            })
            .orElseThrow(() -> new BuildingNotFoundException(id));
    }

    @PatchMapping("/api-v1/locator/renamepoi")
    public POI renamePOI(@RequestBody String newName, @RequestParam int id) {
        return poiRepostory.findById(id)
            .map(poi -> {
                poi.setName(newName);
                POI saved = poiRepostory.save(poi);
                new Thread("saveToFile") {
                    @Override
                    public void run() {
                        try {
                            saveBuildingsToFile();
                            savePOIsToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                return saved;
            })
            .orElseThrow(() -> new POINotFoundException(id));
    }

    @DeleteMapping("/api-v1/locator/deletepoi")
    public void deletePOI(@RequestParam String authPassword, @RequestParam int id) {
        if (authPassword.equals(ProjectApisApplication.authPassword)) {
            if ((Object) poiRepostory.findById(id) != Optional.empty()) {
                poiRepostory.deleteById(id);
                ProjectApisApplication.instance.logger.info("Deleted POI by id: " + id);
                new Thread("saveToFile") {
                    @Override
                    public void run() {
                        try {
                            saveBuildingsToFile();
                            savePOIsToFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } else {
                throw new POINotFoundException(id);
            }
        } else {
            throw new PermissionDeniedException();
        }
    }

    public void readBuildingsFromFile() throws IOException {
        try {
            File path = new File(System.getProperty("user.home") + "/projectapis/db/BuildingRepository-current.json");
            String json = FileUtils.readFileToString(path);

            List<Building> result = new Gson().fromJson(json, new TypeToken<List<Building>>() {
            }.getType());
            buildingRepostory.saveAll(result);
            ProjectApisApplication.instance.logger.info("Loaded Building Repository from: " + System.getProperty("user.home") + "/projectapis/db/BuildingRepostory-current.json");
        } catch (IOException e) {
            if (e.getMessage().equals("File '/home/iwant2tryhard/projectapis/db/BuildingRepostory-current.json' does not exist")) {
                ProjectApisApplication.instance.logger.warn("Storage Files not found; Creating empty files.");
                saveBuildingsToFile();
                ProjectApisApplication.instance.logger.info("Successfully Created Storage files.");
            }
        }
    }

    public void saveBuildingsToFile() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(getAllBuildings());
        File path = new File(System.getProperty("user.home") + "/projectapis/db/BuildingRepoistory-current.json");
        FileOutputStream file = FileUtils.openOutputStream(path);
        try {
            file.write(json.getBytes());
            ProjectApisApplication.instance.logger.info("Saved Building Repository to: " + System.getProperty("user.home") + "/projectapis/db/BuildingRepoistory-current.json");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readPOIsFromFile() throws IOException {
        try {
            File path = new File(System.getProperty("user.home") + "/projectapis/db/POIRepository-current.json");
            String json = FileUtils.readFileToString(path);

            List<POI> result = new Gson().fromJson(json, new TypeToken<List<POI>>() {
            }.getType());
            poiRepostory.saveAll(result);
            ProjectApisApplication.instance.logger.info("Loaded POI Repository from: " + System.getProperty("user.home") + "/projectapis/db/POIRepository-current.json");
        } catch (IOException e) {
            if (e.getMessage().equals("File '/home/iwant2tryhard/projectapis/db/POIRepository-current.json' does not exist")) {
                ProjectApisApplication.instance.logger.warn("Storage Files not found; Creating empty files.");
                savePOIsToFile();
                ProjectApisApplication.instance.logger.info("Successfully Created Storage files.");
            }
        }
    }

    public void savePOIsToFile() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(getAllPOIs());
        File path = new File(System.getProperty("user.home") + "/projectapis/db/POIRepository-current.json");
        FileOutputStream file = FileUtils.openOutputStream(path);
        try {
            file.write(json.getBytes());
            ProjectApisApplication.instance.logger.info("Saved POI Repository to: " + System.getProperty("user.home") + "/projectapis/db/POIRepository-current.json");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
