package top.xlet.sdk.codegen.define;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by jackie on 16-3-31
 */
public class ResponseClassDefine extends PojoInfo {

    public ResponseClassDefine(String packageName, String className, String desc,
                               List<PropertyInfo> properties) {
        super(packageName, className, desc, properties);
    }

    public List<PojoInfo> imports() {
        List<PojoInfo> imports = Lists.newArrayList();
        for (PropertyInfo property : this.getProperties()) {
            if (property.getReference() != null) {
                imports.add(property.getReference());
            }
        }
        return imports;
    }
}
