package com.jfms.engine.api;

public enum Method {
    INIT(1),
    SEND(2),
    EDIT(3),
    PING(4);

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    Method(int value) {
        this.value = value;
    }
}
