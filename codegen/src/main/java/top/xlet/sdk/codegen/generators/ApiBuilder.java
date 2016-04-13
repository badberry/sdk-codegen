package top.xlet.sdk.codegen.generators;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.models.*;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.properties.RefProperty;
import top.xlet.sdk.codegen.define.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by jackie on 16-4-1
 */
public class ApiBuilder {

    private Map<String, PojoInfo> pojos;
    private String url;
    private String basePackage;
    private Generator generator;
    private HttpMethod method;
    private Operation operation;

    public ApiBuilder pojos(Map<String, PojoInfo> pojos) {
        this.pojos = pojos;
        return this;
    }

    public ApiBuilder basePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    public ApiBuilder url(String url) {
        this.url = url;
        return this;
    }

    public ApiBuilder generator(Generator generator) {
        this.generator = generator;
        return this;
    }

    public ApiBuilder method(HttpMethod method){
        this.method = method;
        return this;
    }

    public ApiBuilder operation(Operation operation){
        this.operation = operation;
        return this;
    }

    public void build() throws IOException {
        if (this.method== null || this.operation== null) {
            throw new RuntimeException("method or operation is null!");
        }

        String name = this.operation.getSummary();
        String desc = this.operation.getDescription();
        String method = this.method.toString();

        ResponseClassDefine response = this.response(this.operation);
        RequestClassDefine request = this.request(this.operation, method, desc);
        List<ViewObjectClassDefine> vos = this.vos(request, response);

        ApiInfo api = new ApiInfo(this.url, name, request, response, vos);
        System.out.println(api.toString());
        generator.generate(api);
    }

    public ResponseClassDefine response(Operation apiOperation) {
        Response okResponse = apiOperation.getResponses().get("200");
        if (okResponse == null) {
            throw new RuntimeException("api not ok[200] response");
        }
        if (!okResponse.getSchema().getType().equals("ref")) {
            throw new RuntimeException("not support response type is not ref type!");
        }
        RefProperty ref = (RefProperty) okResponse.getSchema();
        String responseType = ref.getSimpleRef();
        ResponseClassDefine response = (ResponseClassDefine) this.pojos.get(responseType);
        if (response == null) {
            throw new RuntimeException(String.format("response type[%d] is not exists!", responseType));
        }
        return response;
    }

    public RequestClassDefine request(Operation apiOperation, String method, String desc) {
        List<Parameter> parameters = apiOperation.getParameters();

        List<PropertyInfo> properties = Lists.newArrayList();
        List<ParamInfo> urlParams = Lists.newArrayList();
        List<ParamInfo> params = Lists.newArrayList();
        for (Parameter parameter : parameters) {
            String paramName = parameter.getName();
            String paramDesc = parameter.getDescription();
            switch (parameter.getIn()) {
                case "path": {
                    PathParameter pathParameter = (PathParameter) parameter;
                    String pathType = pathParameter.getType();
                    if (pathParameter.getFormat() != null && !pathParameter.getFormat().isEmpty()) {
                        pathType = pathParameter.getFormat();
                    }
                    String languageType = this.generator.getType(pathType);
                    PropertyInfo pathParamProperty = new PropertyInfo(paramName, languageType, paramDesc);
                    properties.add(pathParamProperty);
                    urlParams.add(new ParamInfo(paramName, pathParamProperty));
                    break;
                }
                case "body": {
                    BodyParameter bodyParameter = (BodyParameter) parameter;
                    if (bodyParameter.getSchema() instanceof RefModel) {
                        RefModel bodyRef = (RefModel) bodyParameter.getSchema();
                        String bodyType = bodyRef.getSimpleRef();
                        PropertyInfo bodyParamProperty = new PropertyInfo(paramName, bodyType, paramDesc, this.pojos.get(bodyType));
                        properties.add(bodyParamProperty);
                        params.add(new ParamInfo(paramName, bodyParamProperty));
                    } else {
                        throw new RuntimeException("only support ref type in body parameter.");
                    }
                    break;
                }
                case "query":
                case "formData": {
                    AbstractSerializableParameter normalParameter = (AbstractSerializableParameter) parameter;
                    String normalType = normalParameter.getType();
                    if (normalParameter.getFormat() != null && !normalParameter.getFormat().isEmpty()) {
                        normalType = normalParameter.getFormat();
                    }
                    String languageType = this.generator.getType(normalType);
                    PropertyInfo normalProperty = new PropertyInfo(paramName, languageType, paramDesc);
                    properties.add(normalProperty);
                    params.add(new ParamInfo(paramName, normalProperty));
                    break;
                }
                default:
                    throw new RuntimeException("not support parameter!");
            }
        }
        String requestClassName = apiOperation.getOperationId().replace(String.format("Using%s", method.toUpperCase()), "");
        RequestClassDefine request = new RequestClassDefine(basePackage + ".requests", requestClassName, desc, properties, method,
                this.url.replace("{", "{{").replace("}", "}}"), urlParams, params);
        return request;
    }

    public List<ViewObjectClassDefine> vos(RequestClassDefine request, ResponseClassDefine response) {
        Map<String, ViewObjectClassDefine> voMap = Maps.newHashMap();

        List<PropertyInfo> allProperties = Lists.newArrayList(request.getProperties());
        allProperties.addAll(response.getProperties());

        for (PropertyInfo property : allProperties) {
            if (property.isReference() && property.getReferenceClass() instanceof ViewObjectClassDefine) {
                ViewObjectClassDefine voDefine = (ViewObjectClassDefine) property.getReferenceClass();
                if (!voMap.containsKey(voDefine.getClassName())) {
                    voMap.put(voDefine.getClassName(), voDefine);
                }
            }
        }
        return Lists.newArrayList(voMap.values());
    }


}
