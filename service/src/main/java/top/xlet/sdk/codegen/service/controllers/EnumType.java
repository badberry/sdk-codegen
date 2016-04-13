package top.xlet.sdk.codegen.service.controllers;

/**
 *
 */
public enum EnumType {
    A(1,"A"),
    B(2,"B"),
    C(3,"C");

    private int value;
    private String desc;

    EnumType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
