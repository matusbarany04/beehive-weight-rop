package com.buzzybees.master.tables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


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

    @JsonIgnore
    @Column(name = "status")
    private String status;

    @Column(name = "weight")
    private float weight;

    @Column(name = "temperature")
    private float temperature;

    @Column(name = "humidity")
    private float humidity;

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

    public float getTemperature() {
        return temperature;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getHumidity() {
        return humidity;
    }

    public int getBattery() {
        return battery;
    }

    public void setBeehive(String beehive) {
        this.beehive = beehive;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("weight", weight);
        jsonObject.put("temperature", temperature);
        jsonObject.put("humidity", humidity);
        jsonObject.put("battery", battery);
        return jsonObject;
    }

    public String toCSV() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. MM. yyyy HH:mm:ss");
        String date = simpleDateFormat.format(new Date(timestamp));
        return String.format("%s;%.1f;%d;%.1f;%.1f;%s;", status, weight, battery, temperature, humidity, date);
    }
}
