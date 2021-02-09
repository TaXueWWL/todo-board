package com.snowalker.todo.board.application.command.impl;

import com.snowalker.todo.board.application.command.CommandExecutor;
import com.snowalker.todo.board.application.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.domain.TodoContext;

import java.util.concurrent.CountDownLatch;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:47
 * @className
 * @desc
 */
public class LoginCmdExecutor implements CommandExecutor {

    private TodoContext todoContext;

    public LoginCmdExecutor(TodoContext todoContext) {
        this.todoContext = todoContext;
    }

    @Override
    public void execute(String user, Object extra, CountDownLatch countDownLatch) {
    }

    @Override
    public String commandType() {
        return CommandTokenConstant.LOGIN;
    }
}
