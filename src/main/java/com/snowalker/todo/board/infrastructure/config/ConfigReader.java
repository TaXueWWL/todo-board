package com.snowalker.todo.board.infrastructure.config;

import com.snowalker.todo.board.infrastructure.logger.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author snowalker
 * @createTime: 2021-02-08 11:22
 * @ClassName: ConfigReader
 * @description: 配置读取
 */
public class ConfigReader {

    private static Properties properties;

    /**持久化类型：文件*/
    public static final String FILE = "file";
    /**持久化类型：数据库*/
    public static final String DATABASE = "database";

    private ConfigReader() {
        init();
    }

    public static ConfigReader getInstance() {
        return Holder.singleton;
    }

    private void init() {
        loadProperties();
    }

    private synchronized void loadProperties() {
        properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("app.properties");
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            Logger.error("未找到配置文件app.properties");
        } catch (IOException e) {
            Logger.error("发生IOException");
        } finally {
            try {
                if(null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                Logger.error("app.properties文件流关闭出现异常");
            }
        }
        Logger.debug("加载properties文件内容完成...........");
        Logger.debug("properties文件内容：" + properties);
    }

    public synchronized String getConfig(String key) {
        return getConfig(key, "");
    }

    public synchronized String getConfig(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 内部类，为单例提供
     */
    private static class Holder {
        private static ConfigReader singleton = new ConfigReader();
    }
}
