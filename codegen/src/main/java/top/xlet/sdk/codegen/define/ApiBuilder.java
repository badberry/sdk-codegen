package top.xlet.sdk.codegen.define;

import com.google.common.collect.Lists;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.parameters.AbstractSerializableParameter;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.properties.Property;
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

    private Map<String, ResponseClassDefine> responseDefMap;
    private Map<String, ViewObjectClassDefine> voDefMap;
    private ApiInfo apiInfo = new ApiInfo();

    public ApiBuilder responses(Map<String, ResponseClassDefine> responseDefMap) {
        this.responseDefMap = responseDefMap;
        return this;
    }

    public ApiBuilder vos(Map<String, ViewObjectClassDefine> voDefMap) {
        this.voDefMap = voDefMap;
        return this;
    }

    public ApiBuilder path(String path) {
        LOGGER.info("set api url={}", path);
        apiInfo.url(path);
        return this;
    }

    private List<PropertyInfo> properties = Lists.newArrayList();
    private List<ParamInfo> urlParams = Lists.newArrayList();
    private List<ParamInfo> params = Lists.newArrayList();

    private void params(List<Parameter> parameters) {
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
                    String bodyType = bodyParameter.getSchema().getReference().replace("#/definitions/", "");
                    PropertyInfo bodyParamProperty = new PropertyInfo(paramName, bodyType, paramDesc, this.getReference(bodyType));
                    properties.add(bodyParamProperty);
                    params.add(new ParamInfo(paramName, bodyParamProperty));
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
    }


    private void response(Response response) {

    }

    public ApiInfo build(Path path) {
        Map<HttpMethod, Operation> operationMap = path.getOperationMap();
        if (operationMap.size() > 1) {
            throw new RuntimeException(String.format("api %s not set method", this.apiInfo.getUrl()));
        }

        for (HttpMethod method : operationMap.keySet()) {
            Operation operation = operationMap.get(method);
            String name = operation.getSummary();
            LOGGER.info("get name:{}", name);
            String desc = operation.getDescription();
            LOGGER.info("get desc:{}", desc);
            String methodStr = method.toString();
            LOGGER.info("get method:{}", methodStr);


            List<Parameter> parameters = operation.getParameters();
            LOGGER.info("get params size:{}", parameters.size());
            this.params(parameters);

            Response okResponse = operation.getResponses().get("200");
            if (okResponse == null) {
                throw new RuntimeException("api not ok[200] response");
            }
            if (okResponse.getSchema().getType().equals("ref")) {
                RefProperty ref = (RefProperty) okResponse.getSchema();
                String responseType = ref.getSimpleRef();
            }
        }
        return null;
    }

    private PojoInfo getReference(String type) {
        if (this.voDefMap.containsKey(type)) {
            return this.voDefMap.get(type);
        } else if (this.responseDefMap.containsKey(type)) {
            return this.responseDefMap.get(type);
        } else {
            throw new RuntimeException(String.format("reference type[%s] not found!", type));
        }
    }


}
