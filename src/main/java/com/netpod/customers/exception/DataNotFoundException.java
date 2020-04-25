package com.netpod.customers.exception;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(Throwable cause) {
        super(cause);
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
