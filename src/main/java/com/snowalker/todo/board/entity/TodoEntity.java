package com.snowalker.todo.board.entity;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 15:17
 * @className TodoEntity
 * @desc 待办项目实体
 */
public class TodoEntity {

    /**序号*/
    private int index;
    /**内容*/
    private String content;
    /**是否为最后一个，默认false*/
    private boolean last = false;
    /**是否完成，默认false 未完成*/
    private boolean done = false;

    public TodoEntity() {
    }

    public TodoEntity(int index, String content, boolean last) {
        this(index, content, last, false);
    }

    public TodoEntity(int index, String content, boolean last, boolean done) {
        this.index = index;
        this.content = content;
        this.last = last;
        this.done = done;
    }

    public static TodoEntity newInstance() {
        return new TodoEntity();
    }

    /**
     * 完成
     */
    public void done() {
        this.done = true;
    }

    public int getIndex() {
        return index;
    }

    public TodoEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getContent() {
        return content;
    }

    public TodoEntity setContent(String content) {
        this.content = content;
        return this;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast() {
        this.last = true;
    }


    public void setNotLast() {
        this.last = false;
    }

    public TodoEntity setLast(boolean last) {
        this.last = last;
        return this;
    }

    public boolean isDone() {
        return done;
    }

    public TodoEntity setDone(boolean done) {
        this.done = done;
        return this;
    }
}
