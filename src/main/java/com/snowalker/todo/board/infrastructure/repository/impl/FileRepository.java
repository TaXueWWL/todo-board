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
 * https://blog.csdn.net/quyuquan2014/article/details/82784408
 * FileWriter:
 *  创建一个FileWriter 对象，该对象一被初始化就需要要明确被操纵文件
 *  该文件会被创建到指定目录下，如果该目录下已有了同名文件的话，则会被覆盖。
 *  其实该步就是在明确数据要存放在的目的地
 *  FileWriter fw=new FileWriter("quyuquan1111.txt");
 *
 *  调用write方法，将字符串写入到流中。
 *  fw.write("恩赐");
 *
 *  刷新流对象中的缓冲中的数据 将数据刷到目的地中
 *  fw.flush
 *
 *  关闭流资源，但是关闭之前会刷新一次内部的缓冲中的数据。
 *  将数据刷到目的地中去，和flush区别：flush 刷新后，流可以继续使用，close刷新后，会将流关闭。
 *  fw.close();
 */
public class FileRepository implements IRepository<TodoEntity> {

    /**行号分隔符，通过.区分行号与后面的内容*/
    public static final String LINE_SEPARATOR = ".";

    private String absoluteTodoFilePath = ConfigReader.getInstance().getConfig("repository.file.root_path") +
            ConfigReader.getInstance().getConfig("repository.file.name");

    /**flush后可继续使用*/
    private FileWriter fileWriter;

    private FileWriter overwriteFileWriter;

    public FileRepository() {
//        try {
//            this.fileWriter = new FileWriter(absoluteTodoFilePath, true);
//            this.overwriteFileWriter = new FileWriter(absoluteTodoFilePath, false);
//        } catch (IOException e) {
//            throw new TodoRuntimeException("初始化FileRepository异常!,加载fileWriter失败.", e);
//        }
    }

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
            // todo delete this
            Logger.debug(todoFileList.toString());
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
        int index = line.lastIndexOf(LINE_SEPARATOR);
        String lineNum = line.substring(0, index);
        String content = line.substring(index + 1, line.length());
        return new ImmutablePair<>(lineNum, content);
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
            // 反序列化
            TodoEntity todoEntity = new TodoEntity();
            todoEntity.deSerialize(linePair.getRight());
            originTodoList.add(todoEntity);
        });

        // 根据用户筛选并写入TodoContext
        originTodoList.stream().forEach(todoEntity -> {
            String username = todoEntity.getName();

            Map<String, List<TodoEntity>> todoMap = TodoContext.getTodoMap();

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
        synchronized (FileRepository.class) {
            try {
//                FileWriter fileWriter = new FileWriter(absoluteTodoFilePath, true);
                // 通过这个方式解决中文乱码问题
                BufferedWriter fileWriter = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (absoluteTodoFilePath,true),"UTF-8"));
                String content = new StringBuilder().append(lineNum).append(LINE_SEPARATOR).append(todoEntity.serialize()).toString();
                fileWriter.write(content + "\r\n");
                fileWriter.flush();
                fileWriter.close();
                System.out.println(content);
            } catch (Exception e) {
                Logger.error("FileRepository.append追加写入todoEntity异常!");
            }
        }
    }

    /**
     * 读取到指定位置
     * 替换当前位置  并将前后的内容刷回文件
     * @param todoEntity
     * @return
     */
    @Override
    public boolean update(TodoEntity todoEntity) {

        // 取出全局行号，找到该行 进行替换操作，重新写回文件
        int globalIndex = todoEntity.getGlobalIndex();
        List<String> fileContentList = new ArrayList<>();

        LineNumberReader lineNumberReader = null;
        try {
            //构造LineNumberReader实例
            lineNumberReader = new LineNumberReader(new FileReader(absoluteTodoFilePath));

            String line = null;
            while ((line = lineNumberReader.readLine()) != null) {
                if (lineNumberReader.getLineNumber() == globalIndex) {
                    // 目标行，替换该行拼并写入
                    String newLine = todoEntity.serialize2FileContent();
                    fileContentList.add(newLine);
                } else {
                    // 非目标行直接添加
                    fileContentList.add(line);
                }
            }
        } catch (Exception e) {
            throw new TodoRuntimeException("按行读取失败!");
        } finally {
            //关闭lineNumberReader
            try {
                if (lineNumberReader != null) {
                    lineNumberReader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // 重新写入文件
        overwriteFile(fileContentList);
        return true;
    }

    /**
     * 覆盖写文件
     * @param fileContentList
     */
    private void overwriteFile(List<String> fileContentList) {

        try {
//            FileWriter overwriteFileWriter = new FileWriter(absoluteTodoFilePath, false);
            BufferedWriter overwriteFileWriter = new BufferedWriter (new OutputStreamWriter (new FileOutputStream (absoluteTodoFilePath,false),"UTF-8"));

            fileContentList.stream().forEach(line -> {
                try {
                    overwriteFileWriter.write(line + "\r\n");
                    overwriteFileWriter.flush();
                } catch (IOException e) {
                    throw new TodoRuntimeException("覆盖写文件失败!", e);
                }
            });
            overwriteFileWriter.close();
        } catch (Exception e) {
            throw new TodoRuntimeException("overwriteFile异常!", e);
        }
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
