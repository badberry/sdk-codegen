package top.xlet.sdk.codegen.generators;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import top.xlet.sdk.codegen.define.PojoInfo;
import top.xlet.sdk.codegen.define.PropertyInfo;
import top.xlet.sdk.codegen.define.ResponseClassDefine;
import top.xlet.sdk.codegen.define.ViewObjectClassDefine;

import java.util.List;
import java.util.Map;

/**
 * Created by jackie on 16-4-5
 */
public class DefineAnalyzer {

    private Map<String, Model> definitions;
    private String basePackage;
    private Generator generator;

    public DefineAnalyzer definitions(Map<String, Model> definitions) {
        this.definitions = definitions;
        return this;
    }

    public DefineAnalyzer basePackage(String packageName) {
        this.basePackage = packageName;
        return this;
    }

    public DefineAnalyzer generator(Generator generator) {
        this.generator = generator;
        return this;
    }

    public Map<String, PojoInfo> build() {
        Map<String, PojoInfo> definitionMap = Maps.newHashMap();
        List<PropertyInfo> referenceProperties = Lists.newArrayList();

        for (String className : definitions.keySet()) {
            Model model = definitions.get(className);
            String desc = model.getDescription();

            List<PropertyInfo> classProperties = Lists.newArrayList();
            Map<String, Property> properties = model.getProperties();
            for (String propertyName : properties.keySet()) {
                Property property = properties.get(propertyName);
                String propertyDesc = property.getDescription();
                PropertyInfo propertyDef;
                if (property instanceof RefProperty) {
                    String propertyType = ((RefProperty) property).getSimpleRef();
                    propertyDef = new PropertyInfo(propertyName, propertyType, propertyDesc);
                    referenceProperties.add(propertyDef);
                } else {
                    String propertyType = property.getType();
                    if (property.getFormat() != null && !property.getFormat().isEmpty()) {
                        propertyType = property.getFormat();
                    }
                    String languageType = generator.getType(propertyType);
                    propertyDef = new PropertyInfo(propertyName, languageType, propertyDesc);
                }
                classProperties.add(propertyDef);
            }

            if (className.contains("Response")) {
                String packageName = this.basePackage + ".responses";
                ResponseClassDefine responseClass = new ResponseClassDefine(packageName, className, desc, classProperties);
                definitionMap.put(className, responseClass);
            } else {
                String packageName = this.basePackage + ".vos";
                ViewObjectClassDefine vo = new ViewObjectClassDefine(packageName, className, desc, classProperties);
                definitionMap.put(className, vo);
            }
        }

        for (PropertyInfo propertyDef : referenceProperties) {
            String type = propertyDef.getType();
            if (definitionMap.containsKey(type)) {
                PojoInfo ref = definitionMap.get(type);
                propertyDef.setRef(ref);
            } else {
                throw new RuntimeException(String.format("reference type=%s not found!", type));
            }
        }
        return definitionMap;
    }


}
