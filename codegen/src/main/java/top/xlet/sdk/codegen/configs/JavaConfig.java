package top.xlet.sdk.codegen.configs;

/**
 * java generation config.
 */
public class JavaConfig {

    private String groupId;
    private String artifactId;
    private String version;

    public JavaConfig(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
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
}
