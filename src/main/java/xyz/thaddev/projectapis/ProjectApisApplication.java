package xyz.thaddev.projectapis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.thaddev.projectapis.computercontrolsystem.CommandAPIController;
import xyz.thaddev.projectapis.timersystem.TimerAPIController;
import xyz.thaddev.projectapis.timersystem.TimerInstanceAPIController;

@SpringBootApplication
public class ProjectApisApplication {
	public static final String authPassword = "<Password>";
	public static ProjectApisApplication instance;

	public Logger logger = LoggerFactory.getLogger(ProjectApisApplication.class);

	private TimerAPIController timerAPIController;
	private TimerInstanceAPIController timerInstanceAPIController;
	private CommandAPIController commandAPIController;

	public static void main(String[] args) {
		SpringApplication.run(ProjectApisApplication.class, args);
	}

	public ProjectApisApplication() {
		instance = this;
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
}
