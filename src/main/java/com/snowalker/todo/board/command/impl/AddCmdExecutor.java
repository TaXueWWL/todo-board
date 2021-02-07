package com.snowalker.todo.board.command.impl;

import com.snowalker.todo.board.command.CommandExecutor;
import com.snowalker.todo.board.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.entity.TodoContext;
import com.snowalker.todo.board.entity.TodoEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

        // 参数转换
        List<String> extraList = (ArrayList) extra;
        String extraParam = extraList.get(0);
        if (StringUtils.isBlank(extraParam)) {
            return;
        }

        // 创建一个新的TodoEntity
        TodoEntity lastTodoEntity = todoContext.getLastTodoEntity(user);
        if (!lastTodoEntity.isLast()) {
            // 不存在已有的todo 则当前是第一个
            lastTodoEntity.setIndex(1).setContent(extraParam).setLast();
            this.todoContext.addTodoItem(user, lastTodoEntity);

            // 打印新增的item
            printTodoItem(lastTodoEntity);
        } else {
            // 存在已有的todo 且当前是最后一个 处理当前的index last+1, 设置当前为最后一个，刷新现有最后一个为非最后一个
            int index = lastTodoEntity.getIndex();
            int newIndex = index + 1;
            lastTodoEntity.setNotLast();
            // 创建一个新的todo
            TodoEntity newTodo = new TodoEntity(newIndex, extraParam, true);
            this.todoContext.addTodoItem(user, newTodo);

            // 打印新增的item
            printTodoItem(newTodo);
        }

    }


    private void printTodoItem(TodoEntity todoEntity) {
        System.out.println(todoEntity.getIndex() + ": " + todoEntity.getContent());
        System.out.println("Item <" + todoEntity.getIndex() + "> added." );
    }

    @Override
    public String commandType() {
        return CommandTokenConstant.ADD;
    }
}
