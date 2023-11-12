package com.buzzybees.master.beehives;

import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.tables.Status;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "beehives")
public class Beehive {

    public static final int SIM_MODE = 0;
    public static final int WIFI_MODE = 1;
    public static final int OTHER_BEEHIVE_MODE = 2;

    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "location")
    private String location;

    @Column(name = "latitude")
    private float latitude;

    @Column(name = "longitude")
    private float longitude;

    @Column(name = "name")
    private String name;

    @Column(name = "model")
    private String model;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "other_users")
    private String otherUsers = "{}";

    @Column(name = "connection_mode")
    private int connectionMode = SIM_MODE;

    @Column(name = "interval_min")
    private int interval = 60;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "beehive")
    private final List<Device> devices = new LinkedList<>();

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

    public int getInterval() {
        return interval;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setInterval(int interval) {
        this.interval = interval;
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

    public List<Device> getDevices() {
        return devices;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setSensors(String devicesJSON) {
        JSONObject jsonObject = new JSONObject(devicesJSON);
        for (String key : jsonObject.keySet()) {
            Device device = Device.fromJSON(jsonObject.getJSONObject(key), key);
            device.setBeehive(this);
            devices.add(device);
        }
    }
}
