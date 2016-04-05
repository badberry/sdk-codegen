package top.xlet.sdk.codegen.define;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by jackie on 16-4-5
 */
public class DefineAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefineAnalyzer.class);

    private Map<String, Model> definitions;
    private String basePackage;

    public DefineAnalyzer definitions(Map<String, Model> definitions) {
        this.definitions = definitions;
        return this;
    }


    public DefineAnalyzer basePackage(String packageName) {
        this.basePackage = packageName;
        return this;
    }

    public Map<String, PojoInfo> build() {
        Map<String, PojoInfo> definitionMap = Maps.newHashMap();
        List<PropertyInfo> referenceProperties = Lists.newArrayList();

        for (String className : definitions.keySet()) {
            Model model = definitions.get(className);
            String desc = model.getDescription();
            LOGGER.info("get definition:{},desc:{}", className, desc);

            List<PropertyInfo> classProperties = Lists.newArrayList();
            Map<String, Property> properties = model.getProperties();
            for (String propertyName : properties.keySet()) {
                Property property = properties.get(propertyName);
                String propertyDesc = property.getDescription();
                PropertyInfo propertyDef;
                if (property instanceof RefProperty) {
                    String propertyType = ((RefProperty) property).getSimpleRef();
                    LOGGER.info("get reference property:name={},type={},desc={}", propertyName, propertyType, propertyDesc);
                    propertyDef = new PropertyInfo(propertyName, propertyType, propertyDesc);
                    referenceProperties.add(propertyDef);
                } else {
                    String propertyType = property.getType();
                    LOGGER.info("get reference property:name={},type={},desc={}", propertyName, propertyType, propertyDesc);
                    propertyDef = new PropertyInfo(propertyName, propertyType, propertyDesc);
                }
                classProperties.add(propertyDef);
            }

            if (className.contains("Response")) {
                String packageName = this.basePackage + ".responses";
                ResponseClassDefine responseClass = new ResponseClassDefine(packageName, className, desc, classProperties);
                definitionMap.put(className, responseClass);
                LOGGER.info(responseClass.toString());
            } else {
                String packageName = this.basePackage + ".vos";
                ViewObjectClassDefine vo = new ViewObjectClassDefine(packageName, className, desc, classProperties);
                definitionMap.put(className, vo);
                LOGGER.info(vo.toString());
            }
        }

        for (PropertyInfo propertyDef : referenceProperties) {
            String type = propertyDef.getType();
            if (definitionMap.containsKey(type)) {
                PojoInfo ref = definitionMap.get(type);
                propertyDef.setRef(ref);
            } else {
                LOGGER.error("reference type={} not found!", type);
            }
        }
        return definitionMap;
    }
}
