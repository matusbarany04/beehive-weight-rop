package com.buzzybees.master.controllers.template;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Response of mapping with auto added status and given data
 * Spring will convert this object to json string
 */
public class ApiResponse {

    private String status = "ok";
    private final Map<String, Object> data = new HashMap<>();

    /**
     * create default apiResponse with status
     */
    public ApiResponse() {

    }

    public static ApiResponse OK(){
        return new ApiResponse();
    }

    /**
     * create default apiResponse with status
     * @param status - will add to json
     */
    public ApiResponse(String status) {
        this.status = status;
    }

    /**
     * create single data element apiResponse with status
     * @param key json data key
     * @param value data
     */
    public ApiResponse(String key, Object value) {
        data.put(key, value);
    }

    /**
     * create simple JSON with status and one json object
     * @param key - json key
     * @param json - data
     * @return json string
     */
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
