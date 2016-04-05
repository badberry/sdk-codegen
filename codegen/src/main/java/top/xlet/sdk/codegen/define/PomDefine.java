package top.xlet.sdk.codegen.define;

/**
 *
 */
public class PomDefine {

    private String groupId;
    private String artifactId;
    private String version;

    public PomDefine(String groupId, String artifactId, String version) {
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
