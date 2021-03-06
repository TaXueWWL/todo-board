package com.snowalker.todo.board.application.command;

import com.snowalker.todo.board.domain.TodoContext;

import java.util.concurrent.CountDownLatch;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:39
 * @className
 * @desc
 */
public interface CommandExecutor {

    /**
     * 处理逻辑
     * @param user
     * @param extra
     */
    void execute(String user, Object extra);

    /**
     * 命令类别
     * @return
     */
    String commandType();

    /**
     * 写入todoContext
     * @param todoContext
     */
    void putTodoContext(TodoContext todoContext);
}
