package com.snowalker.todo.board.infrastructure.exception;

/**
 * @author snowalker
 * @createTime: 2021-02-08 12:01
 * @ClassName: UnSupportRepositoryException
 * @description: UnSupportRepositoryException
 */
public class UnSupportRepositoryException extends RuntimeException {

    public UnSupportRepositoryException() {
    }

    public UnSupportRepositoryException(String message) {
        super(message);
    }

    public UnSupportRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnSupportRepositoryException(Throwable cause) {
        super(cause);
    }

    public UnSupportRepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
