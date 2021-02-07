package com.snowalker.todo.board.command.impl;

import com.snowalker.todo.board.command.CommandExecutor;
import com.snowalker.todo.board.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.entity.TodoContext;
import com.snowalker.todo.board.entity.TodoEntity;
import org.apache.commons.lang3.StringUtils;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:46
 * @className AddCmdExecutor
 * @desc 增加todo项
 */
public class AddCmdExecutor implements CommandExecutor {

    private TodoContext todoContext;

    public AddCmdExecutor(TodoContext todoContext) {
        this.todoContext = todoContext;
    }

    @Override
    public void execute(String user, Object extra) {
        if (extra == null) {
            return;
        }
        String extraParam = (String) extra;
        if (StringUtils.isBlank(extraParam)) {
            return;
        }
        // 创建一个新的TodoEntity
        TodoEntity lastTodoEntity = todoContext.getLastTodoEntity(user);
        if (lastTodoEntity.isLast()) {
            // 当前是第一个
            lastTodoEntity.setIndex(1).setContent(extraParam);
        } else {
            // 处理当前的index last+1

        }
        this.todoContext.addTodoItem(user, lastTodoEntity);
    }

    @Override
    public String commandType() {
        return CommandTokenConstant.ADD;
    }
}
