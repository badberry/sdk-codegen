package top.xlet.sdk.codegen.generators;

import top.xlet.sdk.codegen.define.ApiInfo;

import java.io.IOException;

/**
 *
 */
public interface Generator {

    String getType(String type);

    void initProject(String baseDir, String service, String version) throws IOException;

    void generate(ApiInfo api) throws IOException;
}
