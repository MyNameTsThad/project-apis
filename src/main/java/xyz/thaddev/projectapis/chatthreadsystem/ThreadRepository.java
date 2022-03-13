package xyz.thaddev.projectapis.chatthreadsystem;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
}
