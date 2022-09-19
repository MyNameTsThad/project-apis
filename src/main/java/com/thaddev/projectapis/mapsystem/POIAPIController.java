package com.thaddev.projectapis.mapsystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thaddev.projectapis.PermissionDeniedException;
import com.thaddev.projectapis.ProjectApisApplication;
import com.thaddev.projectapis.mapsystem.exceptions.POINotFoundException;
import org.apache.commons.io.FileUtils;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
public class POIAPIController {
    private final POIRepostory poiRepostory;

    public POIAPIController(POIRepostory poiRepostory) {
        this.poiRepostory = poiRepostory;
        ProjectApisApplication.instance.setBuildingAPIController(this);
        new Thread("loadFromFile-mapssystem") {
            @Override
            public void run() {
                try {
                    readPOIsFromFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
        POI saved = poiRepostory.save(toAdd);
        new Thread("saveToFile") {
            @Override
            public void run() {
                try {
                    savePOIsToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        return saved;
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

    public void readPOIsFromFile() throws IOException {
        try {
            File path = new File("/projectapis/db/POIRepository-current.json");
            String json = FileUtils.readFileToString(path);

            List<POI> result = new Gson().fromJson(json, new TypeToken<List<POI>>() {
            }.getType());
            poiRepostory.saveAll(result);
            ProjectApisApplication.instance.logger.info("Loaded POI Repository from: " + "/projectapis/db/POIRepository-current.json");
        } catch (IOException e) {
            if (e.getMessage().equals("File '/projectapis/db/POIRepository-current.json' does not exist")) {
                ProjectApisApplication.instance.logger.warn("Storage Files not found; Creating empty files.");
                savePOIsToFile();
                ProjectApisApplication.instance.logger.info("Successfully Created Storage files.");
            }
        }
    }

    public void savePOIsToFile() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(getAllPOIs());
        File path = new File("/projectapis/db/POIRepository-current.json");
        FileOutputStream file = FileUtils.openOutputStream(path);
        try {
            file.write(json.getBytes());
            ProjectApisApplication.instance.logger.info("Saved POI Repository to: " + "/projectapis/db/POIRepository-current.json");
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
