package com.thaddev.projectapis.postviewscounter;

import com.thaddev.projectapis.ProjectApisApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PostViewsAPIController {
    public PostViewsAPIController() {
        ProjectApisApplication.instance.setPostViewsAPIController(this);
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
    }
}
