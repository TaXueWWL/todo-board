package com.snowalker.todo.board;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 17:08
 * @className
 * @desc 日志工具
 */
public class Logger {

    private static final String WARNING = "[warning] ";

    private static final String DEBUG = "[debug] ";

    private static final String INFO = "[info] ";

    public static void printBanner(String s) {
        System.out.println(s);
    }

    public static void printTips(String s) {
        System.out.print(s);
    }

    public static void warning(String s) {
        System.out.println(WARNING + s);
    }

    public static void debug(String s) {
        System.out.println(DEBUG + s);
    }

    public static void info(String s) {
        System.out.println(INFO + s);
    }
}
