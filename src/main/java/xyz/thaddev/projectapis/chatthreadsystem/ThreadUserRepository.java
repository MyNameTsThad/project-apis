package xyz.thaddev.projectapis.chatthreadsystem;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadUserRepository extends JpaRepository<ThreadUser, Long> {
}
