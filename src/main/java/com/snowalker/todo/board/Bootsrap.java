package com.snowalker.todo.board;

import com.snowalker.todo.board.application.command.constant.CommandTokenConstant;
import com.snowalker.todo.board.application.CommandInputHandler;
import com.snowalker.todo.board.infrastructure.config.ConfigReader;
import com.snowalker.todo.board.infrastructure.logger.Logger;
import com.snowalker.todo.board.domain.repository.RepositoryDelegator;
import org.apache.commons.lang3.StringUtils;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 20:21
 * @className Bootsrap
 */
public class Bootsrap {

    public static void main(String[] args) throws Exception {
        // 前置处理 资源预载
        PreProcess();

        // 输出提示
        Logger.printBanner("================== Welcome to use [todo board] ======================");

        // 初始化一个命令处理器
        CommandInputHandler commandInputHandler = new CommandInputHandler();
        commandInputHandler.printHelpMenu();

        while (true) {
            Logger.printBanner("");
            // 接收命令并解析执行
            Logger.printTips(">>> ");
            String originCommand = commandInputHandler.nextCommand();
            if (StringUtils.isBlank(originCommand) || originCommand.equals(CommandTokenConstant.EXIT)) {
                System.out.println("exit!");
                System.exit(0);
                break;
            }

            // todo 清屏
            if (originCommand.equalsIgnoreCase(CommandTokenConstant.CLEAR)) {
            }

            // 具体处理逻辑
            commandInputHandler.parseCommand(originCommand);
        }
    }

    /**
     * 前置处理 资源预载
     */
    private static void PreProcess() {
        // 加载配置文件
        ConfigReader.getInstance();

        // 加载todo到内存中
        RepositoryDelegator.getInstance().load();
    }

}
