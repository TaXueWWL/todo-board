package com.snowalker.todo.board.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 15:19
 * @className TodoContext
 * @desc 待办项上下文
 */
public class TodoContext {

    private static final Map<String, List<TodoEntity>> todoMap = Maps.newConcurrentMap();

    /**
     * 增加一个新的todo
     * @param user
     * @param todoEntity
     */
    public void addTodoItem(String user, TodoEntity todoEntity) {
        user = checkUser(user);
        List<TodoEntity> todoEntityList = chooseTodoListByUser(user);
        synchronized (todoMap) {
            todoEntityList.add(todoEntity);
        }
    }

    /**
     * 获取最后一个todo
     * @param user
     * @return
     */
    public TodoEntity getLastTodoEntity(String user) {
        user = checkUser(user);
        List<TodoEntity> todoEntityList = chooseTodoListByUser(user);
        // 获取最后一个
        if (todoEntityList.size() > 0) {
            TodoEntity lastTodo = todoEntityList.get(todoEntityList.size() - 1);
            lastTodo.setLast();
            return lastTodo;
        }
        // 新建一个新的
        TodoEntity lastTodo = TodoEntity.newInstance();
        return lastTodo;
    }

    private String checkUser(String user) {
        if (StringUtils.isBlank(user)) {
            user = "default";
        }
        return user;
    }

    private List<TodoEntity> chooseTodoListByUser(String user) {
        List<TodoEntity> todoEntities = todoMap.get(user);
        if (todoEntities == null) {
            todoEntities = new ArrayList<>();
        }
        return todoEntities;
    }

}
