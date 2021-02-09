package com.snowalker.todo.board.handler;

import com.google.common.collect.Lists;
import com.snowalker.todo.board.infrastructure.logger.Logger;
import com.snowalker.todo.board.handler.command.CommandExecutorAdaptor;
import com.snowalker.todo.board.handler.command.constant.CommandTokenConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;


/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 15:26
 * @className
 * @desc 命令输入处理器
 */
public class CommandInputHandler {

    private Scanner scanner;

    private CommandExecutorAdaptor commandExecutorAdaptor;

    private static final List<String> COMMAND_HELP_MENU = Lists.newArrayList();

    public CommandInputHandler(Scanner scanner) {
        this.scanner = scanner;
        // 初始化帮助菜单
        initCommandHelpTable();
        // 初始化命令执行适配器
        initCommandExecutorAdaptor();
    }

    public CommandInputHandler() {
        this.scanner = new Scanner(System.in);
        // 初始化帮助菜单
        initCommandHelpTable();
        // 初始化命令执行适配器
        initCommandExecutorAdaptor();
    }

    public void initCommandHelpTable() {
        COMMAND_HELP_MENU.add("");
        COMMAND_HELP_MENU.add(String.format("%-48s", "* [支持的命令:]") + String.format("%-58s", "|                          *"));
        COMMAND_HELP_MENU.add(String.format("%-50s", "* 添加Todo项") +  String.format("%-40s", "|  todo add <item>         *"));
        COMMAND_HELP_MENU.add(String.format("%-50s", "* 完成Todo项") +  String.format("%-40s", "|  todo done <itemIndex>   *"));
        COMMAND_HELP_MENU.add(String.format("%-38s", "* 查看Todo列表,缺省情况下只列出未完成的Todo项") + String.format("%-40s", "|  todo list               *"));
        COMMAND_HELP_MENU.add(String.format("%-44s", "* 使用all参数,查看所有的Todo项") + String.format("%-40s", "|  todo list --all         *"));
        COMMAND_HELP_MENU.add(String.format("%-49s", "* 用户登录") + String.format("%-40s", "|  todo login -u user      *"));
        COMMAND_HELP_MENU.add(String.format("%-49s", "* 用户退出") + String.format("%-40s", "|  todo logout             *"));
        COMMAND_HELP_MENU.add(String.format("%-49s", "* Todo列表导出") + String.format("%-40s", "|  todo export > todolist  *"));
        COMMAND_HELP_MENU.add(String.format("%-49s", "* Todo列表导入") + String.format("%-40s", "|  todo import -f todolist *"));
        COMMAND_HELP_MENU.add(String.format("%-47s", "* 初始化数据库") + String.format("%-40s", "|  todo init               *"));
        COMMAND_HELP_MENU.add(String.format("%-48s", "* 退出命令行") + String.format("%-40s", "|  exit                    *"));
        COMMAND_HELP_MENU.add("");
    }

    private void initCommandExecutorAdaptor() {
        commandExecutorAdaptor = new CommandExecutorAdaptor();
    }

    /**
     * 读取下一个命令
     * @return
     */
    public String nextCommand() {
        String nextCommand = scanner.nextLine();
        if (StringUtils.isBlank(nextCommand)) {
            return null;
        }
        return nextCommand;
    }

    public void parseCommand(String originCommand) {
        if (StringUtils.isBlank(originCommand)) {
            System.out.println("input command cannot be null! please input another valid command...");
            return;
        }

        // 判断是否为帮助列表
        if (originCommand.equalsIgnoreCase(CommandTokenConstant.HELP)) {
            printHelpMenu();
            return;
        }

        // 解析命令原串为token列表
        List<String> commandTokenList = Lists.newArrayList(Arrays.asList(originCommand.trim().split("\\s+")));
        if (commandTokenList == null || commandTokenList.size() <= 1) {
            Logger.warning( "Current input command \"" + originCommand + "\" not supported! Input [help] to see supported commands.");
            return;
        }

        String cmdStartToken = commandTokenList.get(0);
        if (!cmdStartToken.equalsIgnoreCase(CommandTokenConstant.TODO)) {
            Logger.warning( "Current input command \"" + originCommand + "\" not supported! Input [help] to see supported commands.");
            return;
        }

        // 根据第二个token进行命令分流
        String cmdKeyword = commandTokenList.get(1);
        // 获取其余参数
        List<String> extraParams = extraParams(commandTokenList);

        // 分配给具体执行器
        dispatch(cmdKeyword, extraParams);
    }

    private void dispatch(String keyword, List<String> extraParams) {
        if (!commandExecutorAdaptor.cmdExists(keyword)) {
            Logger.warning( "Current input command \"" + keyword + "\" not supported! Input [help] to see supported commands.");
            return;
        }
        // 选用具体的执行器执行  设置user
        commandExecutorAdaptor.execute(keyword, "", extraParams);
    }

    /**
     * 打印帮助菜单
     */
    public void printHelpMenu() {
        COMMAND_HELP_MENU.stream().forEach(menu -> {
            System.out.println(menu);
        });
    }

    private List<String> extraParams(List<String> commandTokenList) {
        // 每次清理之后 下一个都会变成第一个，因此只需要从头清除就可以
        commandTokenList.remove(0);
        commandTokenList.remove(0);
        return Lists.newArrayList(commandTokenList);
    }
}
