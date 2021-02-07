package com.snowalker.todo.board.entity;

import com.google.common.collect.Maps;
import com.snowalker.todo.board.logger.Logger;
import com.snowalker.todo.board.exception.TodoRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 20:20
 * @className SessionContext
 * @desc 登陆会话
 */
public class SessionContext {

    private static final Map<String, LoginUser> SESSION_CONTEXT = Maps.newConcurrentMap();

    public static final String DEFAULT_USER_NAME = "default";

    /**默认会话1小时有效*/
    public static final long DEFAULT_SESSION_EXPIRE_IN = 3600 * 1000;

    public static Map<String, LoginUser> getSessionContext() {
        return SESSION_CONTEXT;
    }

    /**
     * 新增用户会话
     * @param loginUser
     */
    public void put(LoginUser loginUser) {
        // 判断是否存在
        String userName = loginUser.getName();
        LoginUser onlineUser = getUserByName(loginUser.getName());
        if (onlineUser == null) {
            SESSION_CONTEXT.put(userName, loginUser);
            return;
        }
        // 判断是否有效
        if (onlineUser.isValid(System.currentTimeMillis())) {
            Logger.debug("user:" + userName + ",session valid, do nothing.");
            return;
        }
        // 新增
        Logger.debug("user:" + userName + ",session invalid, Put new one.");
        SESSION_CONTEXT.put(userName, loginUser);
    }

    /**
     * 根据用户名获取登录用户
     * @param name
     * @return
     */
    public LoginUser getUserByName(String name) {
        if (StringUtils.isBlank(name)) {
            name = DEFAULT_USER_NAME;
        }
        return SESSION_CONTEXT.get(name);
    }

    /**
     * 判断当前用户会话是否失效
     * @param name
     * @return
     */
    public boolean isValid(String name) {
        LoginUser loginUser = getUserByName(name);
        if (loginUser == null) {
//            LoginUser loginUser = new LoginUser();
//            loginUser.setName(name).setLoginTimeMills(System.currentTimeMillis()).setExpireInSeconds(DEFAULT_SESSION_EXPIRE_IN);
//            SESSION_CONTEXT.put(name, loginUser);
            throw new TodoRuntimeException("请使用一个用户进行登录!");
        }
        return loginUser.isValid(System.currentTimeMillis());
    }
}
