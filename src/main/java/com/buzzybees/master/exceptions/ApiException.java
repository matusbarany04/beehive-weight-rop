package com.buzzybees.master.exceptions;

import java.util.HashMap;

public abstract class ApiException extends Exception {


    HashMap<String, Object> errorPayload;

    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, HashMap<String, Object> errorPayload) {
        super(message);
        this.errorPayload = errorPayload;
    }


    public HashMap<String, Object> getErrorPayload() {
        return errorPayload;
    }

    public boolean isErrorPayloadEmpty(){
        return errorPayload == null;
    }
}


