package com.grp4.gcash.mini.transactionservice.controller;

public class ChanneNotFoundException extends Exception{
    public ChanneNotFoundException() {
    }

    public ChanneNotFoundException(String message) {
        super(message);
    }

    public ChanneNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChanneNotFoundException(Throwable cause) {
        super(cause);
    }

    public ChanneNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
