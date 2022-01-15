package xyz.thaddev.projectapis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApisApplication {
	public static final String authPassword = "<Password>";
	public static ProjectApisApplication instance;

	public Logger logger = LoggerFactory.getLogger(ProjectApisApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ProjectApisApplication.class, args);
	}

	public ProjectApisApplication() {
		instance = this;
	}
}
