package top.xlet.sdk.codegen.define;

import java.util.List;

/**
 * api info.
 */
public class ApiInfo {

    private String name;
    private RequestClassDefine request;
    private ResponseClassDefine response;
    private List<ViewObjectClassDefine> vos;

    public ApiInfo(String name, RequestClassDefine request, ResponseClassDefine response,
                   List<ViewObjectClassDefine> vos) {
        this.name = name;
        this.request = request;
        this.response = response;
        this.vos = vos;
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
