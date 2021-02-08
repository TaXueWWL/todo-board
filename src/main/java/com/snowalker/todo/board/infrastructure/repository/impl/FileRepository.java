package com.snowalker.todo.board.infrastructure.repository.impl;

import com.alibaba.fastjson.JSON;
import com.snowalker.todo.board.domain.TodoContext;
import com.snowalker.todo.board.domain.TodoEntity;
import com.snowalker.todo.board.domain.exception.TodoRuntimeException;
import com.snowalker.todo.board.infrastructure.config.ConfigReader;
import com.snowalker.todo.board.infrastructure.file.FileHandler;
import com.snowalker.todo.board.infrastructure.logger.Logger;
import com.snowalker.todo.board.infrastructure.repository.IRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author snowalker
 * @createTime: 2021-02-08 11:12
 * @ClassName: FileRepository
 * @description: 文件存储
 */
public class FileRepository implements IRepository<TodoEntity> {

    /**行号分隔符，通过.区分行号与后面的内容*/
    private static final String LINE_SEPARATOR = "$";

    private TodoContext todoContext = TodoContext.getInstance();

    private String absoluteTodoFilePath = ConfigReader.getInstance().getConfig("repository.file.root_path") +
            ConfigReader.getInstance().getConfig("repository.file.name");

    /**
     * 启动时加载，将现有的内容装载到内存中
     * 按行写入
     */
    @Override
    public void load() {
        try {
            // 装载全量todo到内存
            FileHandler fileHandler = FileHandler.getInstance();
            // 创建目录，不存在才创建
            fileHandler.createDir(ConfigReader.getInstance().getConfig("repository.file.root_path"));
            // 创建文件，不存在才创建
            fileHandler.createNewFile(absoluteTodoFilePath);
            // 加载文件到内存
            File loadFile = fileHandler.loadFile(absoluteTodoFilePath);
            // 按行读
            List<String> todoFileList = readFileByLine(loadFile);
            System.out.println(JSON.toJSONString(todoFileList));
            // 转换为todoEntity加载到内存中
            transfer(todoFileList);
        } catch (Exception e) {
            throw new TodoRuntimeException("FileRepository.load() error!", e);
        }
    }

    /**
     * 分割一行
     * left  行号
     * right 内容
     * @param line
     * @return
     */
    public Pair<String, String> splitLine(String line) {
        String[] splitArray = line.trim().split(LINE_SEPARATOR);
        return new ImmutablePair<>(splitArray[0], splitArray[1]);
    }

    /**
     * 转换并加载到内存中
     * @param todoFileList
     */
    private void transfer(List<String> todoFileList) {
        if (todoFileList == null || todoFileList.size() == 0) {
            return;
        }

        List<TodoEntity> originTodoList = new ArrayList<>();
        todoFileList.stream().forEach(line -> {
            Pair<String, String> linePair = splitLine(line);
            System.out.println(linePair);
            // 反序列化
            TodoEntity todoEntity = new TodoEntity();
            todoEntity.deSerialize(linePair.getRight());
            originTodoList.add(todoEntity);
        });

        // 根据用户筛选并写入TodoContext
        originTodoList.stream().forEach(todoEntity -> {
            String username = todoEntity.getName();

            Map<String, List<TodoEntity>> todoMap = todoContext.getTodoMap();

            if (todoMap.keySet().contains(username)) {
                // 若已包含该用户，则直接追加
                todoMap.get(username).add(todoEntity);
            } else {
                // 不包含则写入新的
                List<TodoEntity> newTodoEntitys = new ArrayList<>();
                newTodoEntitys.add(todoEntity);
                todoMap.put(username, newTodoEntitys);
            }
        });
    }

    private List<String> readFileByLine(File loadFile) {
        BufferedReader reader = null;
        try {
            List<String> lineList = new ArrayList<>();
            reader = new BufferedReader(new FileReader(loadFile));
            String line = null;

            int lineNum = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((line = reader.readLine()) != null) {
                // 显示行号
                lineList.add(line);
                lineNum++;
            }
            reader.close();
            return lineList;
        } catch (Exception e) {
            throw new TodoRuntimeException("按行加载todo持久化文件失败!file:" + loadFile.getAbsolutePath(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 追加写
     * @param todoEntity
     */
    @Override
    public void append(TodoEntity todoEntity, int lineNum) {
        String fileName = absoluteTodoFilePath;

        synchronized (FileRepository.class) {
            try {
                FileWriter fileWriter = new FileWriter(fileName, true);
                String content = new StringBuilder().append(lineNum).append(LINE_SEPARATOR).append(todoEntity.serialize()).toString();
                System.out.println(content);
                fileWriter.write(content + "\r\n");
                fileWriter.close();
            } catch (Exception e) {
                Logger.error("FileRepository.append追加写入todoEntity异常!");
            }
        }
    }

    @Override
    public boolean update(TodoEntity todoEntity) {
        return false;
    }

    @Override
    public void exportTodoList(String user) {

    }

    @Override
    public void importTodoList(List<TodoEntity> list) {

    }

    /**
     * 获取最后一行
     * @return
     */
    @Override
    public int lastLine() {
        File file = new File(absoluteTodoFilePath);
        try {
            String lastLine = readLastLine(file, "UTF-8");
            if (StringUtils.isBlank(lastLine)) {
                System.out.println("第一行");
                return 0;
            }
            Pair<String, String> linePair = splitLine(lastLine);
            return Integer.valueOf(linePair.getLeft());
        } catch (IOException e) {
            throw new TodoRuntimeException("获取文件最后一行异常", e);
        }
    }

    public static String readLastLine(File file, String charset) throws IOException {
        if (!file.exists() || file.isDirectory() || !file.canRead()) {
            return null;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            long len = raf.length();
            if (len == 0L) {
                return "";
            } else {
                long pos = len - 1;
                while (pos > 0) {
                    pos--;
                    raf.seek(pos);
                    if (raf.readByte() == '\n') {
                        break;
                    }
                }
                if (pos == 0) {
                    raf.seek(0);
                }
                byte[] bytes = new byte[(int) (len - pos)];
                raf.read(bytes);
                if (charset == null) {
                    return new String(bytes);
                } else {
                    return new String(bytes, charset);
                }
            }
        } catch (FileNotFoundException e) {
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (Exception e2) {
                }
            }
        }
        return null;
    }
}
