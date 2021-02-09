package com.snowalker.todo.board.domain.repository.impl;

import com.snowalker.todo.board.domain.TodoEntity;
import com.snowalker.todo.board.domain.repository.IRepository;

import java.util.List;

/**
 * @author snowalker
 * @createTime: 2021-02-08 11:13
 * @ClassName: DatabaseRepository
 * @description:
 */
public class DatabaseRepository implements IRepository<TodoEntity> {

    @Override
    public void load() {

    }

    @Override
    public void append(TodoEntity todoEntity, int lineNum) {

    }

    @Override
    public boolean update(TodoEntity todoEntity) {
        return false;
    }

    @Override
    public void exportTodoList(String user) {

    }

    @Override
    public void importTodoList(List<TodoEntity> list) {

    }

    @Override
    public int lastLine() {
        return 0;
    }
}
