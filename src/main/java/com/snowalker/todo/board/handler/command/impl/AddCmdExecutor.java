package com.snowalker.todo.board.handler.command.impl;

import com.snowalker.todo.board.handler.command.CommandExecutor;
import com.snowalker.todo.board.handler.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.domain.TodoContext;
import com.snowalker.todo.board.domain.TodoEntity;
import com.snowalker.todo.board.infrastructure.repository.RepositoryDelegator;
import com.snowalker.todo.board.infrastructure.threadpool.ThreadPoolHolder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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
    public void execute(String user, Object extra, CountDownLatch countDownLatch) {
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

        // 持久化Todo引用
        TodoEntity persistTodo = null;
        // 获取当前最后一个Todo
        TodoEntity lastTodoEntity = todoContext.getLastTodoEntity(user);

        // 获取文件最后一行
        int lastLineNum = RepositoryDelegator.getInstance().lastLine();
        int currentLineNum = ++lastLineNum;

        if (!lastTodoEntity.isLast()) {
            // 不存在已有的todo 则当前是第一个
            lastTodoEntity.setName(user).setIndex(1).setGlobalIndex(currentLineNum).setContent(extraParam).setLast();
            this.todoContext.addTodoItem(user, lastTodoEntity);
            // 赋值持久化Todo实体
            persistTodo = lastTodoEntity;
            // 打印新增的item
            printTodoItem(lastTodoEntity);
        } else {
            // 存在已有的todo 且当前是最后一个 处理当前的index last+1, 设置当前为最后一个，刷新现有最后一个为非最后一个
            int index = lastTodoEntity.getIndex();
            int newIndex = index + 1;
            lastTodoEntity.setNotLast();
            // 创建一个新的todo
            TodoEntity newTodo = new TodoEntity(user, newIndex, currentLineNum, extraParam, true);
            this.todoContext.addTodoItem(user, newTodo);
            // 赋值持久化Todo实体
            persistTodo = newTodo;
            // 打印新增的item
            printTodoItem(newTodo);
        }



        // 异步写文件
        asyncAppendTodo(persistTodo, currentLineNum, countDownLatch);
    }

    private void asyncAppendTodo(TodoEntity persistTodo, int lineNum, CountDownLatch countDownLatch) {
        ThreadPoolHolder.getInstance().getFileProcessThreadPool()
                .execute(() -> {
                    RepositoryDelegator.getInstance().append(persistTodo, lineNum);
                    countDownLatch.countDown();
                });
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
