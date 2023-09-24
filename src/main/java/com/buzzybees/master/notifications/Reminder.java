package com.buzzybees.master.notifications;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.json.JSONObject;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "date")
    private String date;

    @Column(name = "time")
    private String time;

    @Column(name = "title")
    private String title;

    @Column(name = "details")
    private String details = "";

    @JsonIgnore
    @Column(name = "user_id")
    private long userId;

    @Column(name = "color")
    private String color = "#a1a1a1";

    @Column(name = "notify_by")
    private int notifyBy = 0;

    @Column(name = "action")
    private String action = "";

    public void setColor(String color) {
        this.color = color;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(long id) {
        userId = id;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setNotifyBy(int notifyBy) {
        this.notifyBy = notifyBy;
    }

    public void setAction(String action) {
        this.action = action;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public long getUserId() {
        return userId;
    }

    public String getColor() {
        return color;
    }

    public int getNotifyBy() {
        return notifyBy;
    }

    public String getAction() {
        return action;
    }
}
