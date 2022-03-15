package xyz.thaddev.projectapis.computercontrolsystem;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class StatusResponse {
    @Id
    private short id;

    private String status; //format: "CONTROLLING_TIMER_CHANGED-EXECUTE_COMMAND-TIMER_LIFECYCLE_CHANGED"

    public StatusResponse() {
    }

    public StatusResponse(String status) {
        //id will be random positive short
        this.id = (short) new Random().nextInt(Short.MAX_VALUE);
        this.status = status;
    }

    public StatusResponse(short id, String status) {
        this.id = id;
        this.status = status;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
