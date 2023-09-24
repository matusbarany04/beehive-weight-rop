package com.buzzybees.master.notifications;

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

    @Column(name = "user_id")
    private long userId;

    @Column(name = "color")
    private String color = "#a1a1a1";

    @Column(name = "notify_by")
    private int notifyBy = 0;

    @Column(name = "action")
    private String action = "";

    public Reminder(String title, String date, String time) {
        this.title = title;
        this.date = date;
        this.time = time;
    }

    public Reminder() {

    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("date", date);
        jsonObject.put("time", time);
        jsonObject.put("title", title);
        jsonObject.put("details", details);
        jsonObject.put("color", color);
        jsonObject.put("action", action);
        jsonObject.put("notifyBy", notifyBy);
        return jsonObject;
    }

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
}
