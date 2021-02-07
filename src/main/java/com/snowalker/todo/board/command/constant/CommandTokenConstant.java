package com.snowalker.todo.board.command.constant;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 15:43
 * @className CommandTokenConstant
 * @desc 命令token常量
 */
public abstract class CommandTokenConstant {

    private CommandTokenConstant() {}

    public static final String ADD = "add";

    public static final String DONE = "done";

    public static final String LIST = "list";

    public static final String LOGIN = "login";

    public static final String LOGOUT ="logout";

    public static final String IMPORT = "import";

    public static final String EXPORT = "export";

    public static final String INIT = "init";

    public static final String HELP = "help";

    public static final String EXIT = "exit";

    public static final String CLEAR = "clear";
}
