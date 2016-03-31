package top.xlet.sdk.codegen.define;

import java.util.List;

/**
 * Created by jackie on 16-3-31
 */
public class ViewObjectClassDefine extends PojoInfo {
    public ViewObjectClassDefine(String packageName, String className,
                                 String desc, List<PropertyInfo> properties) {
        super(packageName, className, desc, properties);
    }
}
