package xyz.thaddev.projectapis.chatthreadsystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ThreadMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String message;
    @OneToOne
    private ThreadUser sender;

    public ThreadMessage() {
    }

    public ThreadMessage(String message, ThreadUser sender) {
        this.message = message;
        this.sender = sender;
    }
}
