package top.xlet.sdk.codegen.define;

import java.util.List;

/**
 * sdk info.
 */
public class SdkInfo {

    private String name;
    private String host;
    private String groupId = "cn.cloudtop.sdk";
    private String artifactId;
    private String version;

    private List<ApiInfo> apis;

    public SdkInfo(String name, String host, String groupId, String version, List<ApiInfo> apis) {
        this.name = name;
        this.host = host;
        this.groupId = groupId;
        this.artifactId = this.name + "-sdk";
        this.version = version;
        this.apis = apis;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public List<ApiInfo> getApis() {
        return apis;
    }
}
