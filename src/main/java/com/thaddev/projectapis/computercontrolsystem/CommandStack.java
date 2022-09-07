package com.thaddev.projectapis.computercontrolsystem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandStack extends JpaRepository<Command, Integer> {
}
