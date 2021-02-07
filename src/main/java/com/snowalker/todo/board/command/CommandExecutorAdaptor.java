package com.snowalker.todo.board.command;

import com.google.common.collect.Maps;
import com.snowalker.todo.board.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.command.impl.*;
import com.snowalker.todo.board.entity.TodoContext;

import java.util.Map;
import java.util.Set;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 16:43
 * @className CommandExecutorAdaptor
 * @desc
 */
public class CommandExecutorAdaptor {

    private static final Map<String, CommandExecutor> COMMAND_EXECUTOR_CONTEXT = Maps.newHashMap();

    static {
        TodoContext todoContext = new TodoContext();
        // 初始化具体命令执行者
        COMMAND_EXECUTOR_CONTEXT.put(CommandTokenConstant.ADD, new AddCmdExecutor(todoContext));
        COMMAND_EXECUTOR_CONTEXT.put(CommandTokenConstant.DONE, new DoneCmdExecutor(todoContext));
        COMMAND_EXECUTOR_CONTEXT.put(CommandTokenConstant.LIST, new ListCmdExecutor(todoContext));
        COMMAND_EXECUTOR_CONTEXT.put(CommandTokenConstant.LOGIN, new LoginCmdExecutor(todoContext));
        COMMAND_EXECUTOR_CONTEXT.put(CommandTokenConstant.LOGOUT, new LogoutCmdExecutor(todoContext));
        COMMAND_EXECUTOR_CONTEXT.put(CommandTokenConstant.EXPORT, new ExportCmdExecutor(todoContext));
        COMMAND_EXECUTOR_CONTEXT.put(CommandTokenConstant.IMPORT, new ImportCmdExecutor(todoContext));
        COMMAND_EXECUTOR_CONTEXT.put(CommandTokenConstant.INIT, new InitDatabaseCmdExecutor(todoContext));

    }

    /**
     * 校验是否支持输入的命令类别
     * @param commandType
     * @return
     */
    public boolean cmdExists(String commandType) {
        Set<String> cmdSet = COMMAND_EXECUTOR_CONTEXT.keySet();
        if (!cmdSet.contains(commandType)) {
            return false;
        }
        return true;
    }

    public void execute(String commandType, String user, Object extra) {
        COMMAND_EXECUTOR_CONTEXT.get(commandType).execute(user, extra);
    }

}
