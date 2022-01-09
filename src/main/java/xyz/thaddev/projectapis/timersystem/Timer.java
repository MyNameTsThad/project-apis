package xyz.thaddev.projectapis.timersystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private long lengthTime;

    public Timer(long lengthTime) {
        this.lengthTime = lengthTime;
    }

    public Timer() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLengthTime() {
        return lengthTime;
    }

    public void setLengthTime(long lengthTime) {
        this.lengthTime = lengthTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timer timer = (Timer) o;
        return id == timer.id &&  lengthTime == timer.lengthTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lengthTime);
    }

    @Override
    public String toString() {
        return "Timer{" +
                "id=" + id +
                ", lengthTime=" + lengthTime +
                '}';
    }
}
