package top.xlet.sdk.codegen.service.controllers;

/**
 * simple api response.
 */
public class SimpleCreateResponse {

    private final SimpleVo simple;

    public SimpleCreateResponse(SimpleVo simple) {
        this.simple = simple;
    }

    public SimpleVo getSimple() {
        return simple;
    }
}
