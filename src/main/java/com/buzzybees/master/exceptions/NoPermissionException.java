package com.buzzybees.master.exceptions;

public class NoPermissionException extends ApiException {
    public NoPermissionException() {
        super("ERR_NO_PERMISSION");
    }
}