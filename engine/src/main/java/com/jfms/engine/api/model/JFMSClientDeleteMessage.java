package com.jfms.engine.api.model;

/**
 * Created by vahid on 4/9/18.
 */
public class JFMSClientDeleteMessage {
    private Integer method;
    private String from;
    private String to;
    private  String id;

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
