package com.snowalker.todo.board.infrastructure.repository;

import com.snowalker.todo.board.domain.TodoEntity;
import com.snowalker.todo.board.domain.exception.TodoRuntimeException;
import com.snowalker.todo.board.domain.exception.UnSupportRepositoryException;
import com.snowalker.todo.board.infrastructure.config.ConfigReader;
import com.snowalker.todo.board.infrastructure.repository.impl.DatabaseRepository;
import com.snowalker.todo.board.infrastructure.repository.impl.FileRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author snowalker
 * @createTime: 2021-02-08 11:33
 * @ClassName: RepositoryDelegator
 * @description: 持久化委派
 */
public class RepositoryDelegator implements IRepository<TodoEntity> {

    /**持久化设施类型  file / database*/
    private String repoType;

    private IRepository repositoryImpl;

    private RepositoryDelegator() {
        // 获取配置文件
        ConfigReader configReader = ConfigReader.getInstance();
        String repository = configReader.getConfig("repository");
        if (StringUtils.isBlank(repository)) {
            throw new TodoRuntimeException("app.properties 未配置repository，请关注!");
        }
        this.repoType = repository;
    }

    public static RepositoryDelegator getInstance() {
        return Holder.singleton;
    }

    private IRepository select(String repoType) {
        if (ConfigReader.FILE.equals(repoType)) {
            repositoryImpl = new FileRepository();
            return repositoryImpl;
        }
        if (ConfigReader.DATABASE.equals(repoType)) {
            repositoryImpl = new DatabaseRepository();
            return repositoryImpl;
        }
        throw new UnSupportRepositoryException("不支持的持久化类型,repoType=" + repoType + ",请参考app.properties中的注释进行配置!");
    }

    @Override
    public void load() {
        select(repoType).load();
    }

    @Override
    public void append(TodoEntity todoEntity, int lineNum) {
        select(repoType).append(todoEntity, lineNum);
    }

    @Override
    public boolean update(TodoEntity todoEntity) {
        return select(repoType).update(todoEntity);
    }

    @Override
    public void exportTodoList(String user) {
        select(repoType).exportTodoList(user);
    }

    @Override
    public void importTodoList(List<TodoEntity> list) {
        select(repoType).importTodoList(list);
    }

    @Override
    public int lastLine() {
        return select(repoType).lastLine();
    }

    /**
     * 内部类，为单例提供
     */
    private static class Holder {
        private static RepositoryDelegator singleton = new RepositoryDelegator();
    }
}
