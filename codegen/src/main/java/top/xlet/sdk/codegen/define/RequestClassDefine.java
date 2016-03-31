package top.xlet.sdk.codegen.define;

import java.util.List;

/**
 * Created by jackie on 16-3-31
 */
public class RequestClassDefine extends PojoInfo {

    private String method;
    private String url;
    private List<ParamInfo> urlParams;
    private List<ParamInfo> params;
    private ResponseClassDefine referenceResponse;

    public RequestClassDefine(String packageName, String className, String desc,
                              List<PropertyInfo> properties, String method, String url, List<ParamInfo> urlParams,
                              List<ParamInfo> params, ResponseClassDefine referenceResponse) {
        super(packageName, className, desc, properties);
        this.method = method;
        this.url = url;
        this.urlParams = urlParams;
        this.params = params;
        this.referenceResponse = referenceResponse;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public List<ParamInfo> getUrlParams() {
        return urlParams;
    }

    public boolean hasUrlParams() {
        return this.urlParams != null && !this.urlParams.isEmpty();
    }

    public List<ParamInfo> getParams() {
        return params;
    }

    public boolean hasParams() {
        return this.params != null && !this.params.isEmpty();
    }

    public ResponseClassDefine getReferenceResponse() {
        return referenceResponse;
    }
}
