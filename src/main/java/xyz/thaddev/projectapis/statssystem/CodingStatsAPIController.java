package xyz.thaddev.projectapis.statssystem;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.thaddev.projectapis.PermissionDeniedException;
import xyz.thaddev.projectapis.ProjectApisApplication;

@RestController
public class CodingStatsAPIController {
    public CodingStatsAPIController() {
        ProjectApisApplication.instance.setCodingStatsAPIController(this);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/api-v1/codingstats/get")
    private CodingStats getCodingStats() {
        return ProjectApisApplication.instance.getCodingStatsManager().getResponse();
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/api-v1/codingstats/sethr")
    private void setHackerrankStats(@RequestBody HackerrankStats stats, @RequestParam String authPassword) {
        if (authPassword.equals(ProjectApisApplication.authPassword))
            ProjectApisApplication.instance.getCodingStatsManager().setHackerrankResponse(stats);
        else throw new PermissionDeniedException();
    }
}
