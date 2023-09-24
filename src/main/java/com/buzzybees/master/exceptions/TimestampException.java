package com.buzzybees.master.exceptions;

public class TimestampException extends ApiException {

    public TimestampException() {
        super("ERR_WRONG_TIMESTAMP");
    }
}
