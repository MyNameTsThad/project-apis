package xyz.thaddev.projectapis.timersystem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TimerInstanceRepository extends JpaRepository<TimerInstance, Integer> {
}
