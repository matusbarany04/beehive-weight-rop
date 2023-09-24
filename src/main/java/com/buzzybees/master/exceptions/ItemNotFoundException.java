package com.buzzybees.master.exceptions;

public class ItemNotFoundException extends Exception {

    public ItemNotFoundException() {
        super("ERR_ITEM_NOT_FOUND");
    }
}
