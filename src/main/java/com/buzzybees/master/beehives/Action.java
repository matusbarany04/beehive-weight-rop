package com.buzzybees.master.beehives;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "actions")
public class Action {

    @JsonProperty("type")
    @Column(name = "type")
    private String type;

    @JsonProperty("execution_time")
    @Column(name = "execution_time")
    private long time = BeehiveActions.NOW;

    @JsonProperty("params")
    @Column(name = "params", columnDefinition = "json", nullable = false)
    private String params = "[]";

    @JsonProperty(value = "author_id",access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "author_id")
    private long author;

    @JsonIgnore
    @Column(name = "done")
    private boolean done = false;

    @JsonProperty("id")
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @JsonProperty(value = "beehive_id" ,access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "beehive_id")
    private String beehive_id;


    public boolean getDone() {
        return this.done;
    }

    public String getBeehive_id() {
        return beehive_id;
    }

    public long getAuthor() {
        return author;
    }

    public String getParams() {
        return params;
    }

    public Action(BeehiveActions actionName) {
        this.type = String.valueOf(actionName);
    }

    public Action(BeehiveActions actionName, long time) {
        this(actionName);
        this.time = time;
    }

    public Action(BeehiveActions actionName, long time, String params, String beehiveId, long authorId) {
        this(actionName, time);
        this.params = params;
        this.author = authorId;
        this.beehive_id = beehiveId;
        this.done = false;
    }

    public Action() {

    }

    public String getType() {
        return type;
    }

    public long getTime() {
        return time;
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



}
