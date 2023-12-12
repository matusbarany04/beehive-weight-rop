package com.buzzybees.master.exceptions;

import java.util.HashMap;

public class InvalidFormException extends ApiException{


    public InvalidFormException() {
        super("INVALID_FORM_EXCEPTION");
    }

    public InvalidFormException(HashMap<String, Object> errorPayload ) {
        super("INVALID_FORM_EXCEPTION", errorPayload);
    }

}
