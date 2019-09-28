package com.netpod.customers.exception;

public class CustomerConverionException extends RuntimeException {
    public CustomerConverionException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
