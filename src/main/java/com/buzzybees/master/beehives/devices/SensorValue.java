package com.buzzybees.master.beehives.devices;

import com.buzzybees.master.tables.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "sensor_values")
public class SensorValue {

    @Id
    @JsonIgnore
    @GeneratedValue
    private long id;

    @Column(name = "status_id")
    private long statusId;

    @JsonProperty("id")
    @Column(name = "sensor_id")
    private long sensorId;

    @Column(name = "value")
    private float value;

    @JsonProperty("port")
    private int port;

    @JsonProperty("type")
    @Enumerated(EnumType.ORDINAL)
    private DeviceType type;

    public long getId() {
        return id;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public float getValue() {
        return value;
    }

    public int getPort() {
        return port;
    }

    public DeviceType getType() {
        return type;
    }

    public void setStatusId(long statusId) {
        this.statusId = statusId;
    }
}
