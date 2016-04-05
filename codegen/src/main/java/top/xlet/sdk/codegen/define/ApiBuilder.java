package top.xlet.sdk.codegen.define;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.models.*;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.properties.RefProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by jackie on 16-4-1
 */
public class ApiBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiBuilder.class);

    private Map<String, PojoInfo> pojos;
    private String url;
    private String basePackage;

    public ApiBuilder pojos(Map<String, PojoInfo> pojos) {
        this.pojos = pojos;
        return this;
    }

    public ApiBuilder basePackage(String basePackage) {
        this.basePackage = basePackage;
        return this;
    }

    public ApiBuilder path(String path) {
        this.url = path;
        return this;
    }

    public ApiInfo build(Path path) {
        Map<HttpMethod, Operation> operationMap = path.getOperationMap();
        if (operationMap.size() > 1) {
            throw new RuntimeException(String.format("api %s not set method", this.url));
        }

        HttpMethod apiMethod = null;
        Operation apiOperation = null;
        for (HttpMethod method : operationMap.keySet()) {
            apiMethod = method;
            apiOperation = operationMap.get(method);
        }

        if (apiMethod == null || apiOperation == null) {
            throw new RuntimeException("method or operation is null!");
        }

        String name = apiOperation.getSummary();
        String desc = apiOperation.getDescription();
        String method = apiMethod.toString();

        ResponseClassDefine response = this.response(apiOperation);
        RequestClassDefine request = this.request(apiOperation, method, desc);
        List<ViewObjectClassDefine> vos = this.vos(request, response);

        return new ApiInfo(this.url, name, request, response, vos);
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
            LOGGER.info("get param:name={},desc={}", paramName, paramDesc);
            switch (parameter.getIn()) {
                case "path":
                    PathParameter pathParameter = (PathParameter) parameter;
                    String pathType = pathParameter.getType();
                    PropertyInfo pathParamProperty = new PropertyInfo(paramName, pathType, paramDesc);
                    properties.add(pathParamProperty);
                    urlParams.add(new ParamInfo(paramName, pathParamProperty));
                    break;
                case "body":
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
                case "query":
                case "formData":
                    AbstractSerializableParameter normalParameter = (AbstractSerializableParameter) parameter;
                    String normalType = normalParameter.getType();
                    PropertyInfo normalProperty = new PropertyInfo(paramName, normalType, paramDesc);
                    properties.add(normalProperty);
                    params.add(new ParamInfo(paramName, normalProperty));
                    break;
                default:
                    throw new RuntimeException("not support parameter!");
            }
        }
        String requestClassName = apiOperation.getOperationId().replace(String.format("Using%s", method.toUpperCase()), "");
        RequestClassDefine request = new RequestClassDefine(basePackage + ".request", requestClassName, desc, properties, method,
                this.url, urlParams, params);
        return request;
    }

    public List<ViewObjectClassDefine> vos(RequestClassDefine request, ResponseClassDefine response) {
        Map<String, ViewObjectClassDefine> voMap = Maps.newHashMap();

        for (PropertyInfo property : request.getProperties()) {
            if (property.isReference() && property.getReferenceClass() instanceof ViewObjectClassDefine) {
                ViewObjectClassDefine voDefine = (ViewObjectClassDefine) property.getReferenceClass();
                if (!voMap.containsKey(voDefine.getClassName())) {
                    voMap.put(voDefine.getClassName(), voDefine);
                }
            }
        }

        for (PropertyInfo property : response.getProperties()) {
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
