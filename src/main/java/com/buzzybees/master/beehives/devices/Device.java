package com.buzzybees.master.beehives.devices;

import com.buzzybees.master.beehives.Beehive;
import jakarta.persistence.*;
import org.json.JSONObject;

@Entity
@Table(name = "device_config")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "beehive")
    private Beehive beehive;

    @Column(name = "port")
    private String port;

    public static Device fromJSON(JSONObject jsonObject, String port) {
        Device device = new Device();
        System.out.println(jsonObject);
        device.name = jsonObject.getString("name");
        device.type = jsonObject.getString("type");
        device.port = port;

        long id = jsonObject.optLong("id");
        if(id > 1) device.id = id;

        return device;
    }

    public String getName() {
        return name;
    }

    public Beehive getBeehive() {
        return beehive;
    }

    public String getPort() {
        return port;
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

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("type", type);
        jsonObject.put("port", port);
        jsonObject.put("id", id);
        return jsonObject;
    }
}
