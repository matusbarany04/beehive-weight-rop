package com.buzzybees.master.exceptions;

public abstract class ApiException extends Exception {

    public ApiException(String message) {
        super(message);
    }
}


