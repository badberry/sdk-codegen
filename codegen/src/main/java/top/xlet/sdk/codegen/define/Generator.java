package top.xlet.sdk.codegen.define;

import java.io.IOException;

/**
 *
 */
public interface Generator {

    void initProject(String baseDir,String service,String version) throws IOException;

    void generate(ApiInfo api) throws IOException;
}
