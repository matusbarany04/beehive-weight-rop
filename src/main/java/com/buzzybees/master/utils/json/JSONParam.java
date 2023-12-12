package com.buzzybees.master.utils.json;

public class JSONParam {

    String name;
    ParamType paramType = ParamType.getDefault();

    boolean required = true;


    public JSONParam(String name, ParamType paramType, boolean required){
        this.name = name;
        this.paramType = paramType;
        this.required = required;
    }

    public JSONParam(String name,boolean required){
        this.name = name;
        this.required = required;
    }

    public JSONParam(String name,ParamType paramType){
        this.name = name;
        this.paramType = paramType;
    }

    public boolean isRequired() {
        return required;
    }

    public ParamType getParamType() {
        return paramType;
    }


    public String getName() {
        return name;
    }

    public boolean isValidType(Object paramValue) {
        return paramType.isValidType(paramValue);
    }
}
