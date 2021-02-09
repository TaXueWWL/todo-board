package com.snowalker.todo.board.application.command.impl;

import com.snowalker.todo.board.application.command.CommandExecutor;
import com.snowalker.todo.board.application.command.constant.CommandTokenConstant;
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

    @Override
    public void execute(String user, Object extra) {
    }

    @Override
    public String commandType() {
        return CommandTokenConstant.INIT;
    }

    @Override
    public void putTodoContext(TodoContext todoContext) {
        this.todoContext = todoContext;
    }
}
