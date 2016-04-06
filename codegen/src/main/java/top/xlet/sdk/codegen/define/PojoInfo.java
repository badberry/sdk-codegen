package top.xlet.sdk.codegen.define;

import com.google.common.collect.Lists;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by jackie on 16-3-30
 */
public abstract class PojoInfo {

    private LocalDateTime createTime;
    private String packageName;
    private String className;
    private String desc;
    private List<PropertyInfo> properties;

    public PojoInfo(String packageName, String className, String desc,
                    List<PropertyInfo> properties) {
        this.packageName = packageName;
        this.className = className;
        this.desc = desc;
        if (properties.size() != 0) {
            PropertyInfo lastProperty = properties.get(properties.size() - 1);
            lastProperty.setLast();
        }
        this.properties = properties;
        this.createTime = LocalDateTime.now();
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getDesc() {
        return desc;
    }

    public List<PropertyInfo> getProperties() {
        return properties;
    }

    public boolean hasProperties() {
        return this.properties != null && !this.properties.isEmpty();
    }

    public List<GetterInfo> getters() {
        List<GetterInfo> getters = Lists.newArrayList();
        for (PropertyInfo property : this.properties) {
            getters.add(new GetterInfo(property));
        }
        return getters;
    }

    protected String baseToString() {
        String classStr = String.format("%s.%s //%s", this.packageName, this.className, this.desc);
        String propertyStr = "";
        for (PropertyInfo property : this.getProperties()) {
            propertyStr += String.format("\t\t%s\n", property);
        }
        return String.format("%s\n%s", classStr, propertyStr);
    }

    @Override
    public String toString() {
        String classStr = String.format("pojo:%s.%s //%s", this.packageName, this.className, this.desc);
        String propertyStr = "";
        for (PropertyInfo property : this.getProperties()) {
            propertyStr += String.format("\t\t%s\n", property);
        }
        return String.format("%s\n%s", classStr, propertyStr);
    }
}
