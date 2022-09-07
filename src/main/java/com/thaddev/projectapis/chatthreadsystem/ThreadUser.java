package com.thaddev.projectapis.chatthreadsystem;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class ThreadUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    private String nickname;
    private String username;
    private String password;

    private long created;

    public ThreadUser(String username, String password) {
        this.nickname = username;
        this.username = username;
        this.password = password;
        this.created = System.currentTimeMillis();
    }
    public ThreadUser(String nickname, String username, String password) {
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.created = System.currentTimeMillis();
    }

    public ThreadUser() {
    }

    public long getUserID() {
        return userID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreadUser that = (ThreadUser) o;
        return Float.compare(that.userID, userID) == 0 && nickname.equals(that.nickname) && username.equals(that.username) && password.equals(that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, nickname, username, password);
    }

    @Override
    public String toString() {
        return "ThreadUser{" +
                "userID=" + userID +
                ", nickname='" + nickname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
