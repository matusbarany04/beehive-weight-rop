package com.buzzybees.master.tables;

import jakarta.persistence.*;
import org.json.JSONObject;


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

    @Column(name = "status")
    private String status;

    @Column(name = "weight")
    private float weight;

    @Column(name = "temperature")
    private float temperature;

    @Column(name = "humidity")
    private float humidity;

    @Column(name = "battery")
    private float battery;

    public Status(JSONObject jsonObject) {
        beehive = jsonObject.getString("beehive");
        status = jsonObject.getString("status");
        temperature = jsonObject.getFloat("temperature");
        humidity = jsonObject.getFloat("humidity");
        timestamp = jsonObject.getLong("timestamp");
        weight = jsonObject.getFloat("weight");
        battery = jsonObject.getInt("battery");
    }

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
}
