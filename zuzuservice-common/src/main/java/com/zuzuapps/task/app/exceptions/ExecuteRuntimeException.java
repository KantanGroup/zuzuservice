package com.zuzuapps.task.app.exceptions;

/**
 * @author tuanta17
 */
public class ExecuteRuntimeException extends Exception {
    private int code;

    public ExecuteRuntimeException(int code) {
        this.code = code;
    }

    public ExecuteRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ExecuteRuntimeException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ExecuteRuntimeException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
