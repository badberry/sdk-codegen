package top.xlet.sdk.codegen.define;

/**
 * Created by jackie on 16-3-31
 */
public class ParamInfo {
    private String  name;
    private PropertyInfo referenceProperty;

    public ParamInfo(String name, PropertyInfo referenceProperty) {
        this.name = name;
        this.referenceProperty = referenceProperty;
    }

    public String getName() {
        return name;
    }

    public PropertyInfo getReferenceProperty() {
        return referenceProperty;
    }
}
