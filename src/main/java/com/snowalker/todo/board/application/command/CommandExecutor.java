package com.snowalker.todo.board.application.command;

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
     * @param countDownLatch
     */
    void execute(String user, Object extra, CountDownLatch countDownLatch);

    /**
     * 命令类别
     * @return
     */
    String commandType();
}
