package com.thaddev.projectapis.postviewscounter;

import com.thaddev.projectapis.ProjectApisApplication;

import java.util.HashMap;
import java.util.Map;

public class PostViewsCounter {
    HashMap<Integer, Integer> postViews = new HashMap<>();

    public PostViewsCounter() {
        ProjectApisApplication.instance.setPostViewsCounter(this);
    }

    public Map<Integer, Integer> getAllPostViews() {
        return postViews;
    }

    public int getPostViews(int strapiID) {
        return postViews.getOrDefault(strapiID, 0);
    }

    public void setPostViews(int strapiID, int views) {
        postViews.put(strapiID, views);
    }

    public void setAllPostViews(Map<Integer, Integer> postViews) {
        this.postViews = new HashMap<>(postViews);
    }

    public void incrementPostViews(int strapiID) {
        setPostViews(strapiID, postViews.getOrDefault(strapiID, 0) + 1);
    }
}
