package xyz.thaddev.projectapis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.thaddev.projectapis.chatthreadsystem.ThreadAPIController;
import xyz.thaddev.projectapis.computercontrolsystem.CommandAPIController;
import xyz.thaddev.projectapis.computercontrolsystem.StatusResponseManager;
import xyz.thaddev.projectapis.statssystem.CodingStatsAPIController;
import xyz.thaddev.projectapis.statssystem.CodingStatsManager;
import xyz.thaddev.projectapis.timersystem.TimerAPIController;
import xyz.thaddev.projectapis.timersystem.TimerInstanceAPIController;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class ProjectApisApplication {
    public static final String authPassword = "<Password>";
    public static final String computerControlAuthPassword = "<Password>";
    public static final String githubAuth = "<AuthToken>";
    public static ProjectApisApplication instance;

    public Logger logger = LoggerFactory.getLogger(ProjectApisApplication.class);

    private TimerAPIController timerAPIController;
    private TimerInstanceAPIController timerInstanceAPIController;
    private CommandAPIController commandAPIController;
    private ThreadAPIController threadAPIController;
    private StatusResponseManager statusResponseManager;
    private CodingStatsAPIController codingStatsAPIController;
    private CodingStatsManager codingStatsManager;

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

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api-v1-javaconfig").allowedOrigins("*");
            }
        };
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
}