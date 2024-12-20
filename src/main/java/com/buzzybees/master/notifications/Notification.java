package com.buzzybees.master.notifications;


import com.buzzybees.master.users.Mailer;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.nio.charset.StandardCharsets;
import java.util.Date;


@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    public enum Type {
        INFO,
        WARNING,
        PROBLEM,
        REMINDER,
    }

    public static final String MESSAGES = "messages.json";
    public static final String LANGUAGE = "SK";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    @JsonIgnore
    @Column(name = "user_id")
    private long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "seen")
    private boolean seen = false;

    @CreatedDate
    @Column(name = "timestamp")
    private Date timestamp;


    public Notification() {

    }

    public Notification(Type type, long userId, String title, String messageName, int value) {
        this.type = type;
        this.title = title;
        this.userId = userId;
        String template = readMessage(messageName);
        if (template != null) message = String.format(template, value);
    }

    public Notification(Type type, long userId, String title, String message) {
        this.type = type;
        this.title = title;
        this.userId = userId;
        this.message = message;
    }

    private String readMessage(String name) {
        try {

            ClassPathResource resource = new ClassPathResource(MESSAGES);
            String content = resource.getContentAsString(StandardCharsets.UTF_8);
            JSONObject messages = new JSONObject(content);
            return messages.getJSONObject(LANGUAGE).getString(name);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public long getUserId() {
        return userId;
    }

    public long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSeen() {
        return seen;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }


    public void sendByMail() {
        UserRepository userRepository = UserService.getBean(UserRepository.class);
        String email = userRepository.getEmail(userId);
        Mailer.sendMessage(email, title, message);
    }

    public void sendToUser() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("message", message);
        jsonObject.put("type", type);
        Notifications.sendPushToUser(userId, jsonObject.toString());
    }

}
