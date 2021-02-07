package com.snowalker.todo.board.command;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:39
 * @className
 * @desc
 */
public interface CommandExecutor {

    /**
     * 处理逻辑
     * @param extra
     * @param user
     */
    void execute(String user, Object extra);

    /**
     * 命令类别
     * @return
     */
    String commandType();
}
