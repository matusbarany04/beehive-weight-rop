package com.buzzybees.master.beehives.actions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "actions")
public class Action {

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private BeehiveActions type;

    @Column(name = "execution_time")
    private long time = BeehiveActions.NOW;

    @Column(name = "params", columnDefinition = "json", nullable = false)
    private String params = "[]";

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

    public String getParams() {
        return params;
    }

    public Action(BeehiveActions actionName) {
        this.type = actionName;
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
        this.status = ActionStatus.PENDING;
    }

    public Action() {

    }

    public BeehiveActions getType() {
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
