package top.xlet.sdk.codegen.service.controllers;

import java.io.Serializable;

/**
 * simple vo.
 */
public class SimpleVo implements Serializable {

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
