package com.snowalker.todo.board;

import com.alibaba.fastjson.JSON;
import com.snowalker.todo.board.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.entity.TodoContext;
import com.snowalker.todo.board.handler.CommandInputHandler;
import com.snowalker.todo.board.logger.Logger;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        CommandInputHandler commandInputHandler = new CommandInputHandler(scanner);

        // 输出提示
        Logger.printBanner("================== Welcome to use [todo board] ======================");
        commandInputHandler.printHelpMenu();

        while (true) {
            Logger.printBanner("=====================================================================");

            // 接收命令并解析执行
            Logger.printTips(">>> ");
            String originCommand = commandInputHandler.nextCommand();
            if (StringUtils.isBlank(originCommand) || originCommand.equals(CommandTokenConstant.EXIT)) {
                System.out.println("exit!");
                break;
            }

            // todo 清屏
            if (originCommand.equalsIgnoreCase(CommandTokenConstant.CLEAR)) {
            }

            // 具体处理逻辑
            commandInputHandler.parseCommand(originCommand);
            Logger.debug(JSON.toJSONString(TodoContext.getTodoMap()));
        }
    }

}
