package com.snowalker.todo.board.exception;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 15:41
 * @className CommandLineParseException
 * @desc
 */
public class TodoRuntimeException extends RuntimeException {

    public TodoRuntimeException() {
    }

    public TodoRuntimeException(String message) {
        super(message);
    }

    public TodoRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TodoRuntimeException(Throwable cause) {
        super(cause);
    }

    public TodoRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
