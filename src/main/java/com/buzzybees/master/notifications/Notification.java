package com.buzzybees.master.notifications;


import com.buzzybees.master.config.SocketHandler;
import com.buzzybees.master.controllers.UserController;
import com.buzzybees.master.users.Mailer;
import com.buzzybees.master.users.UserRepository;
import com.buzzybees.master.users.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.apache.poi.ss.formula.functions.T;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;


@Entity
@Table(name = "notifications")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    public enum Type {
        INFO,
        WARNING,
        PROBLEM
    }

    public static final String MESSAGES = "messages.json";
    public static final String LANGUAGE = "SK";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "type")
    private int type;

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
        this.type = type.ordinal();
        this.title = title;
        this.userId = userId;
        String template = readMessage(messageName);
        if (template != null) message = String.format(template, value);
    }

    public Notification(Type type, long userId, String title, String message) {
        this.type = type.ordinal();
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

    public int getType() {
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
        WebSocketSession session = SocketHandler.getSession(userId);
        TextMessage textMessage = new TextMessage(title + " " + message);
        try {
            if (session.isOpen()) session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
