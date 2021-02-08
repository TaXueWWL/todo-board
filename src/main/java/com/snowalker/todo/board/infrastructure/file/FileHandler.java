package com.snowalker.todo.board.infrastructure.file;

import com.snowalker.todo.board.infrastructure.config.ConfigReader;

import java.io.File;
import java.io.IOException;

/**
 * @author snowalker
 * @createTime: 2021-02-08 15:14
 * @ClassName: FileUtil
 * @description: FileHandler
 */
public class FileHandler {


    private FileHandler() {
    }

    public static FileHandler getInstance() {
        return Holder.singleton;
    }

    /**
     * 创建目录
     * @param dir
     */
    public void createDir(String dir) {
        File file = new File(dir);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }

    public void createNewFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        file.createNewFile();
    }

    /**
     * 删除目录下文件
     * @param dir
     */
    public void removeDirFiles(File dir) {
        if (dir.isFile()) {
            return;
        }

        File[] files=dir.listFiles();
        for(File file:files){
            file.delete();
        }
    }

    /**
     * 加载文件
     * @param fileName
     */
    public File loadFile(String fileName) {
        File file = new File(fileName);
        return file;
    }

    /**
     * 内部类，为单例提供
     */
    private static class Holder {
        private static FileHandler singleton = new FileHandler();
    }

}
