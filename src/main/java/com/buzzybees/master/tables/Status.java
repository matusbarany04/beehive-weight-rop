package com.buzzybees.master.tables;

import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.SensorValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.json.JSONPropertyIgnore;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Status cloneObj() {
        Status status = new Status();
        status.setStatusId(statusId);
        status.setBeehive(beehive);
        status.setTimestamp(timestamp);
        status.setBattery(battery);
        status.setWeight(weight);
        status.setStatus(this.status);
        return status;
    }

    public String toCSV() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. MM. yyyy HH:mm:ss");
        String date = simpleDateFormat.format(new Date(timestamp));
        return String.format("%s;%.1f;%d;%s;", status, weight, battery, date);
    }

    public static class Request extends Status {

        @JsonProperty("sensors")
        private List<SensorValue> sensors;

        public List<SensorValue> getSensorValues() {
            return sensors;
        }

        public Status getBase(){
            return super.cloneObj();
        }
    }
}
