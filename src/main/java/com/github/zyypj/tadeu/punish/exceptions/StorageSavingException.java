package com.github.zyypj.tadeu.punish.exceptions;

public class StorageSavingException extends RuntimeException {

    public StorageSavingException(String message) {
        super(message);
    }

    public StorageSavingException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageSavingException(Throwable cause) {
        super(cause);
    }
}