package com.buzzybees.master.exceptions;

public class OwnershipException extends ApiException {

    public OwnershipException() {
        super("ERR_ACCESS_NOT_PERMITTED");
    }
}
