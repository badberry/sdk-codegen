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


    @ApiModelProperty(value = "唯一性标识")
    public long getId() {
        return id;
    }

    @ApiModelProperty(value = "simple 名称")
    public String getName() {
        return name;
    }
}
