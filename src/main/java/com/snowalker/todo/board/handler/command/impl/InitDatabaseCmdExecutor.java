package com.snowalker.todo.board.handler.command.impl;

import com.snowalker.todo.board.handler.command.CommandExecutor;
import com.snowalker.todo.board.handler.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.domain.TodoContext;

import java.util.concurrent.CountDownLatch;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:53
 * @className InitDatabaseCmdExecutor
 * @desc 初始化数据库
 */
public class InitDatabaseCmdExecutor implements CommandExecutor {

    private TodoContext todoContext;

    public InitDatabaseCmdExecutor(TodoContext todoContext) {
        this.todoContext = todoContext;
    }

    @Override
    public void execute(String user, Object extra, CountDownLatch countDownLatch) {
    }

    @Override
    public String commandType() {
        return CommandTokenConstant.INIT;
    }
}