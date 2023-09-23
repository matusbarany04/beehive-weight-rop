package com.buzzybees.master.beehives;

import com.buzzybees.master.tables.Status;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@Table(name = "beehives")
public class Beehive {

    public static final int SIM_MODE = 0;
    public static final int WIFI_MODE = 1;
    public static final int OTHER_BEEHIVE_MODE = 2;

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private String model;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "other_users")
    private String otherUsers = "{}";

    @Column(name = "token")
    private String token;

    @Column(name = "connection_mode")
    private int connectionMode = SIM_MODE;

    @Column(name = "interval_min")
    private int interval;

    public Beehive() {

    }

    public Beehive (String token, String model) {
        this.token = token;
        this.model = model;
    }

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
        jsonObject.put("model", model);
        return jsonObject;
    }

    public static Beehive findByToken(Beehive[] beehives, String token) {
        for(Beehive beehive : beehives) {
            if(beehive.getToken().equals(token)) return beehive;
        }

        return null;
    }

    public void setConnectionMode(int connectionMode) {
        this.connectionMode = connectionMode;
    }

    public int getConnectionMode() {
        return connectionMode;
    }

    public String getModel() {
        return model;
    }
}
