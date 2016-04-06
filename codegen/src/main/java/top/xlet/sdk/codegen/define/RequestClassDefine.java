package top.xlet.sdk.codegen.define;

import com.google.common.collect.Lists;

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
                              List<ParamInfo> params) {
        super(packageName, className, desc, properties);
        this.method = method;
        this.url = url;
        this.urlParams = urlParams;
        this.params = params;
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

    public void response(ResponseClassDefine response) {
        this.referenceResponse = response;
    }

    public List<PojoInfo> imports() {
        List<PojoInfo> imports = Lists.newArrayList();
        for (PropertyInfo property : this.getProperties()) {
            if (property.isReference()) {
                imports.add(property.getReferenceClass());
            }
        }
        return imports;
    }

    @Override
    public String toString() {
        String propertyStr = "";
        for (PropertyInfo property : this.getProperties()) {
            propertyStr += String.format("\t\t%s\n", property);
        }
        String str = String.format("request:%s.%s //%s\n\t\turl:[%s]%s\n%s",
                this.getPackageName(), this.getClassName(), this.getDesc(),
                this.method, this.url, propertyStr);
        return str;
    }
}
