package com.thaddev.projectapis.postviewscounter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thaddev.projectapis.ProjectApisApplication;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PostViewsAPIController {
    public PostViewsAPIController() {
        ProjectApisApplication.instance.setPostViewsAPIController(this);
        try {
            readFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @CrossOrigin(origins = "*")
    @GetMapping("/api-v1/postviews/getall")
    private Map<Integer, Integer> getAllPostViews() {
        return ProjectApisApplication.instance.getPostViewsCounter().getAllPostViews();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/api-v1/postviews/get")
    private int getPostViews(@RequestParam int strapiID) {
        return ProjectApisApplication.instance.getPostViewsCounter().getPostViews(strapiID);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/api-v1/postviews/increment")
    private void incrementPostViews(@RequestParam int strapiID) {
        ProjectApisApplication.instance.getPostViewsCounter().incrementPostViews(strapiID);
        try {
            saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void setAllPostViews(Map<Integer, Integer> postViews) {
        ProjectApisApplication.instance.getPostViewsCounter().setAllPostViews(postViews);
        try {
            saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveToFile() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(getAllPostViews());
        File path = new File("/projectapis/db/PostViews-current.json");
        FileOutputStream file = FileUtils.openOutputStream(path);
        try {
            file.write(json.getBytes());
            ProjectApisApplication.instance.logger.info("Saved Post Views to: " + "/projectapis/db/PostViews-current.json");
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

    public void readFromFile() throws IOException {
        try {
            File path = new File("/projectapis/db/PostViews-current.json");
            String json = FileUtils.readFileToString(path);

            HashMap<Integer, Integer> obtained = new Gson().fromJson(json, new TypeToken<HashMap<Integer, Integer>>() {
            }.getType());
            setAllPostViews(obtained);

            ProjectApisApplication.instance.logger.info("Loaded Post Views from: " + "/projectapis/db/PostViews-current.json");
        } catch (IOException e) {
            if (e.getMessage().equals("File '/projectapis/db/PostViews-current.json' does not exist")) {
                ProjectApisApplication.instance.logger.warn("Storage Files not found; Creating empty files.");
                saveToFile();
                ProjectApisApplication.instance.logger.info("Successfully Created Storage files.");
            }
        }

    }
}
