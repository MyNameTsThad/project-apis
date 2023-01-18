package com.thaddev.projectapis;

import com.thaddev.projectapis.chatthreadsystem.ThreadAPIController;
import com.thaddev.projectapis.computercontrolsystem.CommandAPIController;
import com.thaddev.projectapis.computercontrolsystem.StatusResponseManager;
import com.thaddev.projectapis.mapsystem.POIAPIController;
import com.thaddev.projectapis.postviewscounter.PostViewsAPIController;
import com.thaddev.projectapis.postviewscounter.PostViewsCounter;
import com.thaddev.projectapis.statssystem.CodingStatsAPIController;
import com.thaddev.projectapis.statssystem.CodingStatsManager;
import com.thaddev.projectapis.timersystem.TimerAPIController;
import com.thaddev.projectapis.timersystem.TimerInstanceAPIController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class ProjectApisApplication {
    public static final String authPassword = System.getenv("PAPI_AUTH_PASSWORD");
    public static final String computerControlAuthPassword = System.getenv("PAPI_COMPUTER_CONTROL_AUTH_PASSWORD");
    public static final String githubAuth = System.getenv("PAPI_GITHUB_AUTH");
    public static ProjectApisApplication instance;

    public Logger logger = LoggerFactory.getLogger(ProjectApisApplication.class);

    private TimerAPIController timerAPIController;
    private TimerInstanceAPIController timerInstanceAPIController;
    private CommandAPIController commandAPIController;
    private ThreadAPIController threadAPIController;
    private StatusResponseManager statusResponseManager;
    private CodingStatsAPIController codingStatsAPIController;
    private CodingStatsManager codingStatsManager;
    private POIAPIController POIAPIController;
    private PostViewsAPIController postViewsAPIController;
    private PostViewsCounter postViewsCounter;


    public static void main(String[] args) {
        SpringApplication.run(ProjectApisApplication.class, args);
    }

    public ProjectApisApplication() {
        instance = this;
        new StatusResponseManager();
        new CodingStatsManager();
        getCodingStatsManager().setHackerrankResponse(3, 5, 225);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startUp() {
        logger.info("Starting TickAll...");
        new Timer("TickAll").scheduleAtFixedRate(new TimerTask() {
            int times = 0;

            @Override
            public void run() {
                times++;
                timerInstanceAPIController.tickAll(times == 10);
                if (times == 10) times = 0;
            }
        }, 1000, 1000);
    }

    public TimerAPIController getTimerAPIController() {
        return timerAPIController;
    }

    public void setTimerAPIController(TimerAPIController timerAPIController) {
        this.timerAPIController = timerAPIController;
    }

    public TimerInstanceAPIController getTimerInstanceAPIController() {
        return timerInstanceAPIController;
    }

    public void setTimerInstanceAPIController(TimerInstanceAPIController timerInstanceAPIController) {
        this.timerInstanceAPIController = timerInstanceAPIController;
    }

    public CommandAPIController getCommandAPIController() {
        return commandAPIController;
    }

    public void setCommandAPIController(CommandAPIController commandAPIController) {
        this.commandAPIController = commandAPIController;
    }

    public ThreadAPIController getThreadAPIController() {
        return threadAPIController;
    }

    public void setThreadAPIController(ThreadAPIController threadAPIController) {
        this.threadAPIController = threadAPIController;
    }

    public StatusResponseManager getStatusResponseManager() {
        return statusResponseManager;
    }

    public void setStatusResponseManager(StatusResponseManager statusResponseManager) {
        this.statusResponseManager = statusResponseManager;
    }

    public CodingStatsAPIController getCodingStatsAPIController() {
        return codingStatsAPIController;
    }

    public void setCodingStatsAPIController(CodingStatsAPIController codingStatsAPIController) {
        this.codingStatsAPIController = codingStatsAPIController;
    }

    public CodingStatsManager getCodingStatsManager() {
        return codingStatsManager;
    }

    public void setCodingStatsManager(CodingStatsManager codingStatsManager) {
        this.codingStatsManager = codingStatsManager;
    }

    public POIAPIController getBuildingAPIController() {
        return POIAPIController;
    }

    public void setBuildingAPIController(POIAPIController POIAPIController) {
        this.POIAPIController = POIAPIController;
    }

    public PostViewsAPIController getPostViewsAPIController() {
        return postViewsAPIController;
    }

    public void setPostViewsAPIController(PostViewsAPIController postViewsAPIController) {
        this.postViewsAPIController = postViewsAPIController;
    }

    public PostViewsCounter getPostViewsCounter() {
        return postViewsCounter;
    }

    public void setPostViewsCounter(PostViewsCounter postViewsCounter) {
        this.postViewsCounter = postViewsCounter;
    }
}