package com.buzzybees.master.tables;

import com.buzzybees.master.beehives.devices.Device;
import com.buzzybees.master.beehives.devices.SensorValue;
import jakarta.persistence.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "status")
public class Status {

    @Column(name = "timestamp")
    private long timestamp;

    @Id
    @GeneratedValue
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

    public String toCSV() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd. MM. yyyy HH:mm:ss");
        String date = simpleDateFormat.format(new Date(timestamp));
        return String.format("%s;%.1f;%d;%s;", status, weight, battery, date);
    }
}
