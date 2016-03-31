package top.xlet.sdk.codegen.define;

/**
 * Created by jackie on 16-3-30
 */
public class PropertyInfo {

    private String name;
    private String type;
    private PojoInfo reference = null;
    private String desc;
    private boolean last = false;

    public PropertyInfo(String name, String type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
    }

    public PropertyInfo(String name, String type, String desc, PojoInfo reference) {
        this(name, type, desc);
        this.reference = reference;
    }

    public PropertyInfo(String name, String type, String desc, boolean last) {
        this(name, type, desc);
        this.last = last;
    }

    public PropertyInfo(String name, String type, String desc, PojoInfo reference, boolean last) {
        this(name, type, desc, last);
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public PojoInfo getReference() {
        return reference;
    }

    public boolean isLast() {
        return last;
    }
}
