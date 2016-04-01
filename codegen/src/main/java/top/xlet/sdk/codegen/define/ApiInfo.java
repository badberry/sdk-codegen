package top.xlet.sdk.codegen.define;

import java.util.List;

/**
 * api info.
 */
public class ApiInfo {

    private String url;
    private String name;
    private RequestClassDefine request;
    private ResponseClassDefine response;
    private List<ViewObjectClassDefine> vos;

    public ApiInfo() {
    }

    void url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public RequestClassDefine getRequest() {
        return request;
    }

    public ResponseClassDefine getResponse() {
        return response;
    }

    public List<ViewObjectClassDefine> getVos() {
        return vos;
    }
}
