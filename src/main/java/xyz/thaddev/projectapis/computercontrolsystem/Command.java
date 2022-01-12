package xyz.thaddev.projectapis.computercontrolsystem;

import xyz.thaddev.projectapis.timersystem.Timer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String execCommand;

    public Command(String execCommand) {
        this.execCommand = execCommand;
    }

    public Command() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExecCommand() {
        return execCommand;
    }

    public void setExecCommand(String execCommand) {
        this.execCommand = execCommand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return id == command.id && execCommand.equals(command.execCommand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, execCommand);
    }

    @Override
    public String toString() {
        return "Command{" +
                "id=" + id +
                ", execCommand='" + execCommand + '\'' +
                '}';
    }
}
