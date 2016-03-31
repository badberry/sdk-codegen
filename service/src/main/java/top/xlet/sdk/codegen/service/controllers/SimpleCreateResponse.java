package top.xlet.sdk.codegen.service.controllers;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponse;

/**
 * simple api response.
 */
@ApiModel(description = "创建simple返回")
public class SimpleCreateResponse {

    private final SimpleVo simple;

    public SimpleCreateResponse(SimpleVo simple) {
        this.simple = simple;
    }

    @ApiModelProperty("simple详细内容")
    public SimpleVo getSimple() {
        return simple;
    }
}
