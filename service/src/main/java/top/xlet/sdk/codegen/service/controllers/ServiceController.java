package top.xlet.sdk.codegen.service.controllers;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * sample controller.
 */
@RestController
public class ServiceController {

    @ApiOperation(value = "创建simple", notes = "创建simple,返回创建是否成功", nickname = "SimpleCreateRequest")
    @RequestMapping(value = "simple", method = RequestMethod.POST, produces = "application/json")
    public SimpleCreateResponse create(SimpleVo simple) {
        return new SimpleCreateResponse(simple);
    }

    @ApiOperation(value = "获取simple", notes = "获取指定id的simple详细内容", nickname = "SimpleGetRequest")
    @RequestMapping(value = "simple/{id:\\d+}", method = RequestMethod.GET, produces = "application/json")
    public SimpleCreateResponse get(@PathVariable("id") long id) {
        return new SimpleCreateResponse(new SimpleVo());
    }
}
