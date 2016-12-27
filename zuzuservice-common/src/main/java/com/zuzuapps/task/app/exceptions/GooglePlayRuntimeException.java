package com.zuzuapps.task.app.exceptions;

/**
 * Google play runtime exception
 */
public class GooglePlayRuntimeException extends Exception {
    private int code;

    public GooglePlayRuntimeException(int code) {
        this.code = code;
    }

    public GooglePlayRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public GooglePlayRuntimeException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public GooglePlayRuntimeException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
