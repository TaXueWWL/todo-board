package com.snowalker.todo.board.application.command.impl;

import com.snowalker.todo.board.application.command.CommandExecutor;
import com.snowalker.todo.board.application.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.domain.TodoContext;
import com.snowalker.todo.board.domain.TodoEntity;
import com.snowalker.todo.board.infrastructure.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:46
 * @className ListCmdExecutor
 * @desc
 */
public class ListCmdExecutor implements CommandExecutor {

    private TodoContext todoContext;

    @Override
    public void execute(String user, Object extra, CountDownLatch countDownLatch) {
        user = todoContext.checkUser(user);
        if (extra == null) {
            return;
        }

        // 参数转换
        List<String> extraList = (ArrayList) extra;
        List<TodoEntity> todoEntities = todoContext.chooseTodoListByUser(user);

        if (extraList.size() == 0) {
            // 默认只打印完成
            handleDefaultUndone(todoEntities);
            return;
        }
        // extraList 参数列表大于0，且是--all则查看所有
        handleAll(extraList, todoEntities);
    }

    /**
     * 查看所有
     * @param extraList
     * @param todoEntities
     */
    private void handleAll(List<String> extraList, List<TodoEntity> todoEntities) {
        String extraParam;
        extraParam = extraList.get(0);
        if (!CommandTokenConstant.LIST_ALL.equalsIgnoreCase(extraParam)) {
            Logger.warning( "Current command token:  <" + extraParam + "> not supported! Input [help] to see supported commands. eg:[list --all]");
            return;
        }
        printAllTodoList(todoEntities);
    }

    /**
     * 查看未完成
     * @param todoEntities
     */
    private void handleDefaultUndone(List<TodoEntity> todoEntities) {
        List<TodoEntity> undoneTodoList;// 缺省状态只列出未完成的list
        undoneTodoList = todoEntities.stream()
                .filter(todoItem -> !todoItem.isDone())
                .collect(Collectors.toList());
        printUndoneTodoList(undoneTodoList);
        return;
    }

    /**
     * 打印所有todo
     * @param todoEntities
     *     1. <item1>
     *     2. <item2>
     *     3. [Done] <item3>
     *
     *     Total: 3 items, 1 item done
     */
    private void printAllTodoList(List<TodoEntity> todoEntities) {
        int total = todoEntities.size();
        // 完成的todo数量
        long doneCount = todoEntities.stream().filter(todoItem -> todoItem.isDone()).count();
        todoEntities.stream().forEach(todo -> {
            if (todo.isDone()) {
                System.out.println(todo.getIndex() + ".  [Done] <" + todo.getContent() + ">");
            } else {
                System.out.println(todo.getIndex() + ". <" + todo.getContent() + ">");
            }
        });
        System.out.println("");
        System.out.println("Total: " + total + " items, " + doneCount + " items done.");
    }

    /**
     * 打印未完成列表
     * @param todoEntityList
     */
    private void printUndoneTodoList(List<TodoEntity> todoEntityList) {
        int total = todoEntityList.size();
        todoEntityList.stream().forEach(todo -> {
            System.out.println(todo.getIndex() + ". <" + todo.getContent() + ">");
        });
        System.out.println("");
        System.out.println("Total: " + total + " items.");
    }

    @Override
    public String commandType() {
        return CommandTokenConstant.LIST;
    }

    @Override
    public void putTodoContext(TodoContext todoContext) {
        this.todoContext = todoContext;
    }
}
