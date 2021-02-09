package com.snowalker.todo.board.application.command;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.snowalker.todo.board.application.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.domain.TodoContext;
import com.snowalker.todo.board.infrastructure.exception.TodoRuntimeException;

import java.util.*;
import java.util.concurrent.CountDownLatch;

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
        TodoContext todoContext = TodoContext.getInstance();
        // 加载命令实现到容器中
        ServiceLoader<CommandExecutor> serviceLoaders =
                ServiceLoader.load(CommandExecutor.class);
        for (CommandExecutor commandExecutor : serviceLoaders) {
            commandExecutor.putTodoContext(todoContext);
            COMMAND_EXECUTOR_CONTEXT.put(commandExecutor.commandType(), commandExecutor);
        }
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

    public boolean needCountDownLatch(String cmdKeyword) {
        List<String> cmdWhiteList = new ArrayList<>();
        cmdWhiteList.add(CommandTokenConstant.ADD);
        return cmdWhiteList.contains(cmdKeyword);
    }
}
