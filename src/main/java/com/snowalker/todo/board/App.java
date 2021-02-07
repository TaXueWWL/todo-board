package com.snowalker.todo.board;

import com.snowalker.todo.board.handler.CommandInputHandler;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Logger.printBanner("================== Welcome to use [todo board] ======================");

        Scanner scanner = new Scanner(System.in);
        CommandInputHandler commandInputHandler = new CommandInputHandler(scanner);

        // 首次输出一次提示
        commandInputHandler.printHelpMenu();
        while (true) {
            Logger.printBanner("====================================================================");
            Logger.printTips(">>> ");

            // 接收命令并解析执行
            String originCommand = commandInputHandler.nextCommand();
            commandInputHandler.parseCommand(originCommand);

            if (StringUtils.isBlank(originCommand) || originCommand.equals("exit")) {
                System.out.println(">>> " + "exit！");
                break;
            }
        }
    }

}
