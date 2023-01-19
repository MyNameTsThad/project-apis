package com.thaddev.projectapis.postviewscounter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thaddev.projectapis.ProjectApisApplication;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostViewsCounter {
    HashMap<Integer, Integer> postViews = new HashMap<>();

    public PostViewsCounter() {
        ProjectApisApplication.instance.setPostViewsCounter(this);
        try {
            readFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Integer> getAllPostViews() {
        return postViews;
    }

    public int getPostViews(int strapiID) {
        return postViews.getOrDefault(strapiID, 0);
    }

    private void setPostViews(int strapiID, int views) {
        postViews.put(strapiID, views);
        try {
            saveToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void incrementPostViews(int strapiID) {
        setPostViews(strapiID, postViews.getOrDefault(strapiID, 0) + 1);
    }

    public void saveToFile() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(postViews);
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

            postViews = new Gson().fromJson(json, new TypeToken<HashMap<Integer, Integer>>() {
            }.getType());
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
