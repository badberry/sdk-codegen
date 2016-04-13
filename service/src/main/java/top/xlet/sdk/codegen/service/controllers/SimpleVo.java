package top.xlet.sdk.codegen.service.controllers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * simple vo.
 */
@ApiModel(description = "Simple 简单对象")
public class SimpleVo implements Serializable {

    private long id;
    private String name;

    private byte aByte;
    private short aShort;
    private int anInt;
    private long aLong;
    private char aChar;
    private float aFloat;
    private double aDouble;
    private boolean aBoolean;
    private EnumType aEnum;

    @ApiModelProperty(value = "唯一性标识")
    public long getId() {
        return id;
    }

    @ApiModelProperty(value = "simple 名称")
    public String getName() {
        return name;
    }

    @ApiModelProperty("byte 例子")
    public byte getaByte() {
        return aByte;
    }

    @ApiModelProperty("short 例子")
    public short getaShort() {
        return aShort;
    }

    @ApiModelProperty("int 例子")
    public int getAnInt() {
        return anInt;
    }

    @ApiModelProperty("long 例子")
    public long getaLong() {
        return aLong;
    }

    @ApiModelProperty("char 例子")
    public char getaChar() {
        return aChar;
    }

    @ApiModelProperty("float例子")
    public float getaFloat() {
        return aFloat;
    }

    @ApiModelProperty("double例子")
    public double getaDouble() {
        return aDouble;
    }

    @ApiModelProperty("boolean 例子")
    public boolean isaBoolean() {
        return aBoolean;
    }

    @ApiModelProperty("enum 例子")
    public EnumType getaEnum() {
        return aEnum;
    }
}
