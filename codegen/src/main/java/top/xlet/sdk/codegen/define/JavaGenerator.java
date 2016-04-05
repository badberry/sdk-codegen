package top.xlet.sdk.codegen.define;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 */
public class JavaGenerator implements Generator {


    @Override
    public void initProject(String baseDir, String service, String version) throws IOException {
        String projectPath = baseDir + File.separator + service + "-service-sdk-java";
        File projectDir = new File(projectPath);
        if (!projectDir.exists()) {
            projectDir.mkdir();
        }

        PomDefine pomDef = new PomDefine("cn.cloudtop.sdks", service + "-sdk", version);

        MustacheFactory mf = new DefaultMustacheFactory();
        //todo:add deploy config in pom.xml
        Mustache mustache = mf.compile("java/pom.xml.mustache");
        mustache.execute(new FileWriter(projectDir + File.separator + "pom.xml"), pomDef).flush();

        String sourcePath = projectDir + File.separator + "src" + File.separator + "main";
        File sourceDir = new File(sourcePath);
        if (!sourceDir.exists()) {
            sourceDir.mkdirs();
        }
    }

    @Override
    public void generate(ApiInfo api) {

    }
}
