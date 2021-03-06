package com.jfms.message_history.dal.entity;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table
public class ChannelUpdateEntity {
    @PrimaryKeyColumn(
            name = "id",
            type = PrimaryKeyType.PARTITIONED,
            ordinal = 0
    )
    private UUID id = UUID.randomUUID();

    @PrimaryKeyColumn(
            name = "editor",
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 1
    )
    private String editor;
    @PrimaryKeyColumn(
            name = "messageId",
            type = PrimaryKeyType.CLUSTERED,
            ordinal = 2
    )
    private String messageId;

    private Long updateTime;
    private String previousValue;
    private String currentValue;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
}
