package xyz.thaddev.projectapis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectApisApplication {
	public static final String authPassword = "<Password>";

	public static void main(String[] args) {
		SpringApplication.run(ProjectApisApplication.class, args);
	}

}
