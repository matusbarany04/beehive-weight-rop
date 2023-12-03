package com.buzzybees.master.beehives.devices;

import com.buzzybees.master.beehives.Beehive;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.json.JSONObject;

@Entity
@Table(name = "device_config")
public class Device {

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beehive")
    private Beehive beehive;

    @Id
    @JsonProperty("id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DeviceType type;

    @Column(name = "port")
    private String port;

    public String getName() {
        return name;
    }

    public Beehive getBeehive() {
        return beehive;
    }

    public String getPort() {
        return port;
    }

    @JsonIgnore
    public int getPortIndex() {
        return Integer.parseInt(port.substring(1)) - 1;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBeehive(Beehive beehive) {
        this.beehive = beehive;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPort(int portIndex) {
        this.port = "S" + (portIndex + 1);
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
