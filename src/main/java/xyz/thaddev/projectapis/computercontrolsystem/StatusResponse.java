package xyz.thaddev.projectapis.computercontrolsystem;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Random;

@Entity
public class StatusResponse {
    @Id
    private long id;

    private String status; //format: "CONTROLLING_TIMER_CHANGED-EXECUTE_COMMAND-TIMER_LIFECYCLE_CHANGED"

    public StatusResponse() {
    }

    public StatusResponse(String status) {
        //id will be random positive long
        this.id = Math.abs(new Random().nextLong());
        this.status = status;
    }

    public StatusResponse(long id, String status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
