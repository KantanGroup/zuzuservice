package com.zuzuapps.task.app.exceptions;

/**
 * App store runtime exception
 */
public class AppStoreRuntimeException extends Exception {
    private int code;

    public AppStoreRuntimeException(int code) {
        this.code = code;
    }

    public AppStoreRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AppStoreRuntimeException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public AppStoreRuntimeException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
