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
public class ExportCmdExecutor implements CommandExecutor {

    private TodoContext todoContext;

    @Override
    public void execute(String user, Object extra) {
    }

    @Override
    public String commandType() {
        return CommandTokenConstant.EXPORT;
    }

    @Override
    public void putTodoContext(TodoContext todoContext) {
        this.todoContext = todoContext;
    }
}
