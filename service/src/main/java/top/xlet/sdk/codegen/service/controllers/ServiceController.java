package top.xlet.sdk.codegen.service.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * sample controller.
 */
@RestController
public class ServiceController {

    @RequestMapping(value = "simple", method = RequestMethod.POST, produces = "application/json")
    public SimpleCreateResponse create(SimpleVo sample) {
        return new SimpleCreateResponse(sample);
    }
}
