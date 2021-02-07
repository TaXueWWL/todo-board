package com.snowalker.todo.board.exception;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 15:41
 * @className CommandLineParseException
 * @desc
 */
public class CommandLineParseException extends RuntimeException {

    public CommandLineParseException() {
    }

    public CommandLineParseException(String message) {
        super(message);
    }

    public CommandLineParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandLineParseException(Throwable cause) {
        super(cause);
    }

    public CommandLineParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
