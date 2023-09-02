package com.buzzybees.master.tables;

import com.buzzybees.master.security.PasswordUtils;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "verified")
    private boolean verified;

    @Column(name = "password")
    private String password;


    @Column(name = "dashboard_data", columnDefinition = "json", nullable = false)
    private String dashboardData = "[]";

    @Column(name = "settings", columnDefinition = "json", nullable = false)
    private String settings = "[]";

    public void setDashboardData(String dashboardData) {
        this.dashboardData = dashboardData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = hashPasswd(password);
    }

    public void updateSettings(String settings) {
        this.settings = settings;
    }

    public String getSettings() {
        return settings;
    }

    private String hashPasswd(String password) throws NoSuchAlgorithmException {
        return PasswordUtils.hashPasswd(password);
    }

    @Override
    public String toString() {
        System.out.println(password);
        return String.format("name = %s, id = %d, type = %s, email = %s", name, id, type, email);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("email", email);
        jsonObject.put("id", id);
        jsonObject.put("type", type);
        jsonObject.put("verified", verified);
        jsonObject.put("dashboardData", dashboardData);
        return jsonObject;
    }
}
