package com.practice.utils;


public enum ChatResponseFormatEnum {
    JSON_OBJECT("json_object"),
    TEXT("text");

    private String value;

    ChatResponseFormatEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
