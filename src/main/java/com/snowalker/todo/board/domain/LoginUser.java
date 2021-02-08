package com.snowalker.todo.board.domain;

import com.snowalker.todo.board.infrastructure.logger.Logger;
import com.snowalker.todo.board.domain.exception.TodoRuntimeException;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 20:21
 * @className LoginUser
 * @desc 登陆用户
 */
public class LoginUser {

    /**
     * 用户名
     */
    private String name;
    /**
     * 会话有效期，单位毫秒 默认1小时
     */
    private Long expireInSeconds = 3600000L;
    /**
     * 登陆时间，单位毫秒
     */
    private Long loginTimeMills;

    /**
     * 会话是否有效  true 会话有效
     * false 会话无效
     * @param currentTimeMills
     * @return
     */
    public boolean isValid(long currentTimeMills) {
        long deltaTimeMills = currentTimeMills - loginTimeMills;
        if (deltaTimeMills < 0) {
            throw new TodoRuntimeException("登陆时间设置有误!发生时钟回拨!");
        }

        // 过期
        if (deltaTimeMills > expireInSeconds) {
            Logger.debug("name:" + name + ",session has expire!");
            return false;
        }
        return true;
    }

    public Long getLoginTimeMills() {
        return loginTimeMills;
    }

    public LoginUser setLoginTimeMills(Long loginTimeMills) {
        this.loginTimeMills = loginTimeMills;
        return this;
    }

    public String getName() {
        return name;
    }

    public LoginUser setName(String name) {
        this.name = name;
        return this;
    }

    public Long getExpireInSeconds() {
        return expireInSeconds;
    }

    public LoginUser setExpireInSeconds(Long expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
        return this;
    }
}
