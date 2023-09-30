package com.buzzybees.master.beehives;

public class Action {

    private String name;
    private Object value;

    public Action(String name) {
        this.name = name;
    }

    public Action(String name, Object value) {
        this(name);
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
