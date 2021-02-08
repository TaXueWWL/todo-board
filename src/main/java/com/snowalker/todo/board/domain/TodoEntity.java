package com.snowalker.todo.board.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.io.IOException;

/**
 * @author snowalker
 * @version 1.0
 * @date 2021/2/7 15:17
 * @className TodoEntity
 * @desc 待办项目实体
 */
public class TodoEntity {

    private String name;
    /**序号*/
    private int index;
    /**内容*/
    private String content;
    /**是否为最后一个，默认false*/
    private boolean last = false;
    /**是否完成，默认false 未完成*/
    private boolean done = false;

    /**全局index*/
    private int globalIndex;

    public TodoEntity() {
    }

    public TodoEntity(String name, int index, int globalIndex, String content, boolean last) {
        this(name, index, globalIndex, content, last, false);
    }

    public TodoEntity(String name, int index, int globalIndex, String content, boolean last, boolean done) {
        this.name = name;
        this.index = index;
        this.globalIndex = globalIndex;
        this.content = content;
        this.last = last;
        this.done = done;
    }

    public static TodoEntity newInstance() {
        return new TodoEntity();
    }

    /**
     * 序列化为json
     * @return
     */
    public String serialize() {
        ImmutableMap<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("index", this.getIndex())
                .put("name", this.getName())
                .put("content", this.getContent())
                .put("last", this.last)
                .put("done", this.done)
                .put("globalIndex", this.getGlobalIndex())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        // 返回序列化消息Json串
        String ret_string = null;
        try {
            ret_string = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("serialize error.", e);
        }
        return ret_string;
    }

    /**
     * 反序列化为对象
     * @param json
     */
    public void deSerialize(String json) {
        Preconditions.checkNotNull(json);
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(json);
            this.setIndex(jsonNode.get("index").asInt());
            this.setName(jsonNode.get("name").asText());
            this.setContent(jsonNode.get("content").asText());
            this.setLast(jsonNode.get("last").asBoolean());
            this.setDone(jsonNode.get("done").asBoolean());
            this.setGlobalIndex(jsonNode.get("globalIndex").asInt());
        } catch (IOException e) {
            throw new RuntimeException("deSerialize error.", e);
        }
    }

    public int getGlobalIndex() {
        return globalIndex;
    }

    public TodoEntity setGlobalIndex(int globalIndex) {
        this.globalIndex = globalIndex;
        return this;
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

    public String getName() {
        return name;
    }

    public TodoEntity setName(String name) {
        this.name = name;
        return this;
    }
}
