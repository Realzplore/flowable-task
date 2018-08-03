package com.flowable.modules.form.enumeration;

/**
 * @Author: liping.zheng
 * @Date: 2018/8/3
 */
public enum FieldType {

    LONG("long"),
    DOUBLE("double"),
    STRING("string"),
    INTEGER("integer"),
    BOOLEAN("boolean");


    private String filedType;

    FieldType(String filedType) {
        this.filedType = filedType;
    }
}
