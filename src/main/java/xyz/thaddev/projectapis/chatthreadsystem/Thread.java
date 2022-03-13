package xyz.thaddev.projectapis.chatthreadsystem;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Thread {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;
    private String description;
    private long creatorID;
    private long created;
    private boolean archived;

    //latest message in this thread
    @OneToOne
    private ThreadMessage lastMessage;
    //variable for all the messages in this thread
    @OneToMany
    private List<ThreadMessage> messages;
    @OneToMany
    private List<ThreadMessage> pinnedMessages;

    public Thread() {
    }

    public Thread(String name, String description, ThreadUser creator) {
        this.name = name;
        this.description = description;
        this.messages = new ArrayList<>();
        this.pinnedMessages = new ArrayList<>();
        this.creatorID = creator.getUserID();
        this.created = System.currentTimeMillis();
    }
    //new thread with no description
    public Thread(String name, ThreadUser creator) {
        this.name = name;
        this.creatorID = creator.getUserID();
        this.messages = new ArrayList<>();
        this.pinnedMessages = new ArrayList<>();
        this.created = System.currentTimeMillis();
    }
    //new thread with starting pinned message
    public Thread(String name, ThreadUser creator, ThreadMessage pinnedMessage) {
        this.name = name;
        this.creatorID = creator.getUserID();
        this.messages = new ArrayList<>();
        this.pinnedMessages = new ArrayList<>();
        this.messages.add(pinnedMessage);
        this.pinnedMessages.add(pinnedMessage);
        this.lastMessage = pinnedMessage;
        this.created = System.currentTimeMillis();
    }
    //new thread with starting pinned message and description
    public Thread(String name, ThreadUser creator, ThreadMessage pinnedMessage, String description) {
        this.name = name;
        this.creatorID = creator.getUserID();
        this.messages = new ArrayList<>();
        this.pinnedMessages = new ArrayList<>();
        this.messages.add(pinnedMessage);
        this.pinnedMessages.add(pinnedMessage);
        this.lastMessage = pinnedMessage;
        this.description = description;
        this.created = System.currentTimeMillis();
    }

    //getters and setters but the id is only a getter

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(long creatorID) {
        this.creatorID = creatorID;
    }

    public ThreadMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(ThreadMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public List<ThreadMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ThreadMessage> messages) {
        this.messages = messages;
    }

    public List<ThreadMessage> getPinnedMessages() {
        return pinnedMessages;
    }

    public void setPinnedMessages(List<ThreadMessage> pinnedMessages) {
        this.pinnedMessages = pinnedMessages;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public long getCreated() {
        return created;
    }
}
