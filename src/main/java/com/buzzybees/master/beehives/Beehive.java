package com.buzzybees.master.beehives;

import com.buzzybees.master.tables.Status;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@Table(name = "beehives")
public class Beehive {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "user_id")
    private long userId;

    @Column(name = "other_users")
    private String otherUsers = "{}";

    @Column(name = "token")
    private String token;

    @Column(name = "interval_min")
    private int interval;

    private boolean isPaired;

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public String getOtherUsers() {
        return otherUsers;
    }

    public String getToken() {
        return token;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static JSONArray mergeStatuses(Status[] statuses) {
        JSONArray jsonArray = new JSONArray();
        for(Status status : statuses) {
            jsonArray.put(status.toJSON());
        }
        return jsonArray;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("interval", interval);
        jsonObject.put("location", location);
        jsonObject.put("token", token);
        return jsonObject;
    }

    public static Beehive findByToken(Beehive[] beehives, String token) {
        for(Beehive beehive : beehives) {
            if(beehive.getToken().equals(token)) return beehive;
        }

        return null;
    }
}
