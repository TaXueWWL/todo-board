package com.snowalker.todo.board.handler.command.impl;

import com.snowalker.todo.board.handler.command.CommandExecutor;
import com.snowalker.todo.board.handler.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.domain.TodoContext;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:47
 * @className
 * @desc
 */
public class LogoutCmdExecutor implements CommandExecutor {

    private TodoContext todoContext;

    public LogoutCmdExecutor(TodoContext todoContext) {
        this.todoContext = todoContext;
    }

    @Override
    public void execute(String user, Object extra) {

    }

    @Override
    public String commandType() {
        return CommandTokenConstant.LOGOUT;
    }
}
