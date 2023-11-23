package com.buzzybees.master.beehives.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;

import java.util.HashMap;

@Entity
@Table(name = "actions")
public class Action {

    public static final int NOW = 0;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ActionType type;

    @JsonProperty("execution_time") // toto tu musi byt inak mi nejde routa, post
    @Column(name = "execution_time")
    private long execution_time = 0;

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
    private String beehive_id;


    public ActionStatus getStatus() {
        return this.status;
    }

    public String getBeehive_id() {
        return beehive_id;
    }

    public long getAuthor() {
        return author;
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getParams() throws JsonProcessingException {
        return new ObjectMapper().readValue(this.params, HashMap.class);
    }

    public Action(ActionType actionName) {
        this.type = actionName;
    }

    public Action(ActionType actionName, long time) {
        this(actionName);
        this.execution_time = time;
    }

    public Action(ActionType actionName, long time, String params, String beehiveId, long authorId) {
        this(actionName, time);
        this.params = params;
        this.author = authorId;
        this.beehive_id = beehiveId;
        this.status = ActionStatus.PENDING;
    }

    public Action() {

    }

    public ActionType getType() {
        return type;
    }

    public long getExecution_time() {
        return execution_time;
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
}
