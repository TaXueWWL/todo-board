package com.snowalker.todo.board;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 17:08
 * @className
 * @desc 日志工具
 */
public class Logger {

    public static final String WARNING = "[warning] ";

    public static void printBanner(String s) {
        System.out.println(s);
    }

    public static void printTips(String s) {
        System.out.print(s);
    }

    public static void warning(String s) {
        System.out.println(WARNING + s);
    }
}
