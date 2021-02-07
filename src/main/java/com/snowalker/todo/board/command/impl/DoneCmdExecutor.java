package com.snowalker.todo.board.command.impl;

import com.snowalker.todo.board.command.CommandExecutor;
import com.snowalker.todo.board.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.entity.TodoContext;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:46
 * @className DoneCmdExecutor
 * @desc
 */
public class DoneCmdExecutor implements CommandExecutor {

    private TodoContext todoContext;

    public DoneCmdExecutor(TodoContext todoContext) {
        this.todoContext = todoContext;
    }

    @Override
    public void execute(String user, Object extra) {

    }

    @Override
    public String commandType() {
        return CommandTokenConstant.DONE;
    }
}
