package com.buzzybees.master.notifications;

import com.buzzybees.master.beehives.actions.Action;
import com.buzzybees.master.beehives.actions.ActionType;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Entity
@Table(name = "reminders")
public class Reminder {

    public static final String DATE_FORMAT =  "yyyy-MM-dd HH:mm";

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

    @OneToOne
    @JoinColumn(name = "action")
    private Action action;

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

    public void setAction(Action action) {
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

    @JsonIgnore
    public Date getDateObject() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            return simpleDateFormat.parse(date + " " + time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public Action getAction() {
        return action;
    }

    public boolean isUpcoming() {
        return getDateObject().compareTo(new Date()) >= 0;
    }

    public void activate() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Notification notification = new Notification(Notification.Type.REMINDER, userId, title, details);
                NotificationRepository repository = DatabaseController.accessRepo(Notification.class);
                repository.save(notification);
                notification.sendToUser();
            }
        }, getDateObject());
    }
}
