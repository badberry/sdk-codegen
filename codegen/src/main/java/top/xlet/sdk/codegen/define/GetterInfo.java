package top.xlet.sdk.codegen.define;

/**
 * Created by jackie on 16-3-30
 */
public class GetterInfo extends PropertyInfo {
    private String property;

    public GetterInfo(PropertyInfo property) {
        super(property.getName(), property.getType(), property.getDesc(), property.getReferenceClass(), property.isLast());
        this.property = property.getName();
    }

    @Override
    public String getName() {
        char[] nameChars = super.getName().toCharArray();
        nameChars[0] -= 32;
        return String.valueOf(nameChars);
    }

    public String getProperty() {
        return property;
    }

    @Override
    public String toString() {
        return String.format("getter:%s %s{%s}//%s", this.getType(),this.getName(), this.property,this.getDesc());
    }
}
