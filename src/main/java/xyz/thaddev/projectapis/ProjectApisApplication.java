package xyz.thaddev.projectapis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.thaddev.projectapis.chatthreadsystem.ThreadAPIController;
import xyz.thaddev.projectapis.computercontrolsystem.CommandAPIController;
import xyz.thaddev.projectapis.timersystem.TimerAPIController;
import xyz.thaddev.projectapis.timersystem.TimerInstanceAPIController;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class ProjectApisApplication {
	public static final String authPassword = "<Password>";
	public static final String computerControlAuthPassword = "<Password>";
	public static ProjectApisApplication instance;

	public Logger logger = LoggerFactory.getLogger(ProjectApisApplication.class);

	private TimerAPIController timerAPIController;
	private TimerInstanceAPIController timerInstanceAPIController;
	private CommandAPIController commandAPIController;
	private ThreadAPIController threadAPIController;

	public static void main(String[] args) {
		SpringApplication.run(ProjectApisApplication.class, args);
	}

	public ProjectApisApplication() {
		instance = this;
		new Timer("TickAll").scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				timerInstanceAPIController.tickAll();
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
}
