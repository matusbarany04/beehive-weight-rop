package com.buzzybees.master.beehives.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import org.json.JSONObject;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Entity
@Table(name = "actions")
@EntityListeners(AuditingEntityListener.class)
public class Action {

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ActionType type;

    @Column(name = "execution_time")
    private long executionTime = 0;

    @Column(name = "params", columnDefinition = "json", nullable = false)
    private String params = "{}";


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "author_id")
    private long author;

    @JsonIgnore
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ActionStatus status = ActionStatus.PENDING;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "beehive_id")
    private String beehive;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public ActionStatus getStatus() {
        return this.status;
    }

    public String getBeehive() {
        return beehive;
    }

    public long getAuthor() {
        return author;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getParams() {
        try {
            return new ObjectMapper().readValue(this.params, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @JsonIgnore
    public String getParamsJSON() {
        return params;
    }

    public Action() {

    }

    public Action(ActionType actionName) {
        this.type = actionName;
    }

    public Action(ActionType type, long executionTime) {
        this(type);
        this.executionTime = executionTime;
    }

    public Action(ActionType type, long executionTime, String params, String beehive, long author) {
        this(type, executionTime);
        this.params = params;
        this.author = author;
        this.beehive = beehive;
        this.status = ActionStatus.PENDING;
    }

    public Action(ActionType actionType, String params, String beehiveId) {
        this(actionType, Actions.NOW, params, beehiveId, Actions.AUTHOR_SYSTEM);
    }

    public JSONObject jsonifyForFrontend(){
        JSONObject output = new JSONObject();

        output.put("type", type);
        output.put("id", id);
        output.put("status", status);
        output.put("beehive_id", beehive);
        output.put("createdAt", createdAt);
        output.put("updatedAt", updatedAt);
        output.put("params", params);
        output.put("executionTime", executionTime);

        return output;
    }

    public ActionType getType() {
        return type;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setAuthor(long author) {
        this.author = author;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setStatus(ActionStatus status) {
        this.status = status;
    }

    public void setParamsMap(HashMap<String, Object> params) {
        try {
            this.params = new ObjectMapper().writeValueAsString(params);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
