package com.buzzybees.master.beehives;

import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.controllers.template.DatabaseController;
import com.buzzybees.master.tables.Status;
import com.buzzybees.master.websockets.ClientMessage;
import com.buzzybees.master.websockets.ClientSocketHandler;
import com.buzzybees.master.websockets.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "beehives")
public class Beehive {

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

    @Column(name = "device_state")
    @Enumerated(EnumType.STRING)
    private BeehiveState deviceState;

    @Column(name = "connection_mode")
    @Enumerated(EnumType.STRING)
    private ConnectionMode connectionMode = ConnectionMode.GSM;

    @Column(name = "interval_min")
    private int interval = 60;

    @Column(name = "linked_to")
    private String linkedTo;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "beehive")
    private final List<Device> devices = new LinkedList<>();

    @Column(name = "wifi_ssid")
    private String wifi_ssid;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String sim_password;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String wifi_password;


    public Beehive() {

    }

    public Beehive (String token, String model) {
        this.token = token;
        this.model = model;
        deviceState = BeehiveState.OFFLINE;
        location = "";
        name = "";
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

    public void setConnectionMode(ConnectionMode connectionMode) {
        this.connectionMode = connectionMode;
    }

    public ConnectionMode getConnectionMode() {
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

    public String getWifiSSID() {
        return wifi_ssid;
    }

    public void setWifi_ssid(String wifi_ssid) {
        this.wifi_ssid = wifi_ssid;
    }

    public String getSim_password() {
        return sim_password;
    }

    public void setSim_password(String sim_password) {
        this.sim_password = sim_password;
    }

    public String getWifi_password() {
        return wifi_password;
    }

    public void setWifi_password(String wifi_password) {
        this.wifi_password = wifi_password;
    }

    public String getLinkedTo() {
        return linkedTo;
    }

    public void setLinkedTo(String linkedTo) {
        this.linkedTo = linkedTo;
    }

    public BeehiveState getState() {
        return deviceState;
    }

    public void setDeviceState(BeehiveState deviceState) {
        this.deviceState = deviceState;
    }

    public void updateState(BeehiveState deviceState) {
        BeehiveRepository beehiveRepository = DatabaseController.accessRepo(getClass());
        setDeviceState(deviceState);
        HashMap<String, Object> params = new HashMap<>();
        params.put("state", deviceState);
        params.put("beehive", token);
        ClientSocketHandler.sendMessageToUser(userId, new ClientMessage(MessageType.UPDATE_DEVICE_STATE, params));
        beehiveRepository.save(this);
    }

    public Beehive createBackup(Beehive oldBeehive) {
        Beehive beehive = new Beehive();
        beehive.token = token;
        beehive.name = name;
        beehive.location = location;
        beehive.model = model;
        beehive.longitude = longitude;
        beehive.latitude = latitude;
        beehive.userId = userId;
        beehive.connectionMode = connectionMode;
        beehive.deviceState = oldBeehive.getState();
        beehive.sim_password = oldBeehive.getSim_password();
        beehive.wifi_password = oldBeehive.getWifi_password();
        beehive.wifi_ssid = oldBeehive.getWifiSSID();
        beehive.interval = oldBeehive.getInterval();
        return beehive;
    }
}
