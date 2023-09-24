package com.buzzybees.master.controllers.template;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse {

    private String status = "ok";
    private final Map<String, Object> data = new HashMap<>();

    public ApiResponse() {

    }

    public ApiResponse(String status) {
        this.status = status;
    }

    public ApiResponse(String key, Object value) {
        data.put(key, value);
    }

    public ApiResponse(String key, Map<String, Object> map) {
        data.put(key, map);
    }

    public static String json(String key, JSONObject json) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", "ok");
        jsonObject.put(key, json);
        return jsonObject.toString();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return data;
    }

    @JsonAnySetter
    public void putObject(String name, Object value) {
        data.put(name, value);
    }
}
