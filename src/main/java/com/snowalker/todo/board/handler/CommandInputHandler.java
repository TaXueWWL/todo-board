package com.snowalker.todo.board.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.snowalker.todo.board.Logger;
import com.snowalker.todo.board.command.CommandExecutorAdaptor;
import com.snowalker.todo.board.command.constant.CommandTokenConstant;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 15:26
 * @className
 * @desc 命令输入处理器
 */
public class CommandInputHandler {

    private Scanner scanner;

    private static final List<String> COMMAND_HELP_MENU = Lists.newArrayList();

    public CommandInputHandler(Scanner scanner) {
        this.scanner = scanner;
        initCommandHelpTable();
    }

    public void initCommandHelpTable() {
        COMMAND_HELP_MENU.add("");
        COMMAND_HELP_MENU.add("* [支持的命令:]                                                      *");
        COMMAND_HELP_MENU.add("* 添加 Todo 项                                 todo add <item>       *");
        COMMAND_HELP_MENU.add("* 完成 Todo 项                                 todo done <itemIndex> *");
        COMMAND_HELP_MENU.add("* 查看 Todo 列表,缺省情况下只列出未完成的Todo项  todo list             *");
        COMMAND_HELP_MENU.add("* 使用 all 参数,查看所有的Todo项                todo list --all       *");
        COMMAND_HELP_MENU.add("* 用户登录                                     todo login -u user     *");
        COMMAND_HELP_MENU.add("* 用户退出                                     todo logout            *");
        COMMAND_HELP_MENU.add("* Todo 列表导出                                todo export > todolist *");
        COMMAND_HELP_MENU.add("* Todo 列表导入                                todo import -f todolist*");
        COMMAND_HELP_MENU.add("* 初始化数据库                                 todo init              *");
        COMMAND_HELP_MENU.add("");
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

        // 根据第二个token进行命令分流
        String cmdKeyword = commandTokenList.get(1);
        // 获取其余参数
        List<String> extraParams = extraParams(commandTokenList);

        // 分配给具体执行器
        dispatch(cmdKeyword, extraParams);
    }

    private void dispatch(String keyword, List<String> extraParams) {
        CommandExecutorAdaptor commandExecutorAdaptor = new CommandExecutorAdaptor();
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
