package com.buzzybees.master.tables;

import com.buzzybees.master.beehives.devices.SensorValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


@Entity
@Table(name = "status")
public class Status {

    @Column(name = "timestamp")
    private long timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long statusId;

    @Column(name = "beehive")
    private String beehive;

    @Column(name = "battery")
    private int battery;

    @Column(name = "status")
    private String status;

    @Column(name = "weight")
    private float weight;

    @JsonProperty("sensors")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "status")
    private List<SensorValue> sensorValues = new LinkedList<>();

    public Status() {

    }

    public String getBeehive() {
        return beehive;
    }

    public Long getStatusId() {
        return statusId;
    }

    public String getStatus() {
        return status;
    }

    public float getWeight() {
        return weight;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getBattery() {
        return battery;
    }

    public void setBeehive(String beehive) {
        this.beehive = beehive;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }

    public String toCSV() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. MM. yyyy HH:mm:ss");
        String date = simpleDateFormat.format(new Date(timestamp));
        return String.format("%s;%.1f;%d;%.1f;%.1f;%s;", status, weight, battery, "", "", date);
    }

    public List<SensorValue> getSensorValues() {
        return sensorValues;
    }
}
