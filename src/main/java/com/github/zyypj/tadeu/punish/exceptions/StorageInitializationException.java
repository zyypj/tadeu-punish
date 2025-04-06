package com.github.zyypj.tadeu.punish.exceptions;

public class StorageInitializationException extends RuntimeException {

    public StorageInitializationException(String message) {
        super(message);
    }

    public StorageInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageInitializationException(Throwable cause) {
        super(cause);
    }
}