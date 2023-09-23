package com.buzzybees.master.beehives.devices;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

@Entity
@Table(name = "scannings")
public class Scan {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "beehive")
    private String beehive;

    @Column(name = "date")
    private Date date;

    @Column(name = "devices", columnDefinition="json")
    private String devices;

    public void setDevices(JSONObject devices) {
       this.devices = devices.toString();
    }

    public void setBeehive(String beehive) {
        this.beehive = beehive;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public String getBeehive() {
        return beehive;
    }

    public Date getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
