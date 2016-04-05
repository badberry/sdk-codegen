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

    public ApiInfo(String url, String name, RequestClassDefine request,
                   ResponseClassDefine response, List<ViewObjectClassDefine> vos) {
        this.url = url;
        this.name = name;
        this.request = request;
        this.request.response(response);
        this.response = response;
        this.vos = vos;
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
