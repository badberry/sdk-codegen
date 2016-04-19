package top.xlet.sdk.codegen.define;

/**
 * Created by jackie on 16-3-30
 */
public class PropertyInfo {

    private String name;
    private String type;
    private boolean reference = false;
    private PojoInfo referenceClass = null;
    private String desc;
    private boolean last = false;

    public PropertyInfo(String name, String type, String desc) {
        this.name = name;
        this.type = type;
        this.desc = desc;
    }

    public PropertyInfo(String name, String type, String desc, PojoInfo referenceClass) {
        this(name, type, desc);
        this.reference = true;
        this.referenceClass = referenceClass;
    }

    public PropertyInfo(String name, String type, String desc, boolean last) {
        this(name, type, desc);
        this.last = last;
    }

    public PropertyInfo(String name, String type, String desc, PojoInfo referenceClass, boolean last) {
        this(name, type, desc, last);
        this.reference = true;
        this.referenceClass = referenceClass;
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

    public boolean isReference() {
        return reference;
    }

    public PojoInfo getReferenceClass() {
        return referenceClass;
    }

    public boolean isLast() {
        return last;
    }

    public void setRef(PojoInfo ref) {
        this.reference = true;
        this.referenceClass = ref;
    }

    @Override
    public String toString() {
        return String.format("property[last:%s]:%s %s //%s", String.valueOf(this.last), this.type, this.name, this.desc);
    }

    public void setLast() {
        this.last = true;
    }
}
