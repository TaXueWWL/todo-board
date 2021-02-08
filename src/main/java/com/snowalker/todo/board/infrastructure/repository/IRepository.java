package com.snowalker.todo.board.infrastructure.repository;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @createTime: 2021-02-08 11:04
 * @ClassName: IRepository
 * @description: 持久化接口
 */
public interface IRepository<T> {

    /**
     * 文件加载
     */
    void load();

    /**
     * 追加一条
     * @param t
     */
    void append(T t, int lineNum);

    /**
     * 修改一条
     * @param t
     * @return
     */
    boolean update(T t);

    /**
     * 为某个用户导出
     * @param user
     */
    void exportTodoList(String user);

    /**
     * 为某个用户导入
     * @param list
     */
    void importTodoList(List<T> list);

    /**
     * 获取最后一条行号
     * @return
     */
    int lastLine();
}
