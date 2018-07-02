package com.github.riteshkukreja.modal;

public enum MethodType {
    GET("get"),
    POST("post");

    private String value;

    MethodType(String str) {
        this.value = str;
    }

    public String getValue() {
        return value;
    }
}
