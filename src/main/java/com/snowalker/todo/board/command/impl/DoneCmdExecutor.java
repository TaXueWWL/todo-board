package com.snowalker.todo.board.command.impl;

import com.snowalker.todo.board.command.CommandExecutor;
import com.snowalker.todo.board.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.entity.TodoContext;
import com.snowalker.todo.board.entity.TodoEntity;
import com.snowalker.todo.board.logger.Logger;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

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
        user = todoContext.checkUser(user);
        if (extra == null) {
            return;
        }

        // 参数转换
        List<String> extraList = (ArrayList) extra;
        String extraParam = extraList.get(0);
        if (StringUtils.isBlank(extraParam)) {
            return;
        }

        // 校验是否为数字
        if (!NumberUtils.isDigits(extraParam)) {
            Logger.warning("<itemIndex> is " + extraParam + "index is Not Number! please check!");
            return;
        }

        // 获取todo列表, List中的下标为index - 1
        int parsedIndex = Integer.valueOf(extraParam);
        List<TodoEntity> todoEntityList = todoContext.chooseTodoListByUser(user);
        if (todoEntityList.size() < parsedIndex) {
            Logger.warning("Current user:" + user + "'s todoList length < " + parsedIndex + ",Please input right index!");
            return;
        }

        // 获取列表中的todo项  并修改为完成
        TodoEntity todoEntity = todoEntityList.get(parsedIndex - 1);
        todoEntity.done();

        printTodoItem(todoEntity);
        // todo  持久化
    }

    /**
     * 打印
     * @param todoEntity
     */
    private void printTodoItem(TodoEntity todoEntity) {
        System.out.println("Item <" + todoEntity.getIndex() + "> done." );
    }

    @Override
    public String commandType() {
        return CommandTokenConstant.DONE;
    }
}
