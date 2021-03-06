package com.jfms.engine.api.model;

import com.jfms.engine.api.Method;

/**
 * Created by vahid on 4/4/18.
 */
public class JFMSServerEditMessage {

    private String id;
    private String from;
    private String body;
    private String subject;
    private Long editTime;
    private Integer method;

    public JFMSServerEditMessage(String id, String from, String body, String subject, Long editTime, Integer method) {
        this.id = id;
        this.from = from;
        this.body = body;
        this.subject = subject;
        this.editTime = editTime;
        this.method = method;
    }

    public JFMSServerEditMessage() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getEditTime() {
        return editTime;
    }

    public void setEditTime(Long editTime) {
        this.editTime = editTime;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }
}
