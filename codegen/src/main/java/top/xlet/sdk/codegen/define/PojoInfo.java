package top.xlet.sdk.codegen.define;

import com.google.common.collect.Lists;
import org.joda.time.LocalDateTime;

import java.util.List;

/**
 * Created by jackie on 16-3-30
 */
public class PojoInfo {

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


}
