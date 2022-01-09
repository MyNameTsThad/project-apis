package xyz.thaddev.projectapis.timersystem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerRepository extends JpaRepository<Timer, Integer> {
}
