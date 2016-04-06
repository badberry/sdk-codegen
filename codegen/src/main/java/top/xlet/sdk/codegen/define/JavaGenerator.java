package top.xlet.sdk.codegen.define;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 */
public class JavaGenerator implements Generator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaGenerator.class);

    private String projectPath;
    private String sourcePath;

    @Override
    public void initProject(String baseDir, String service, String version) throws IOException {
        this.projectPath = baseDir + File.separator + service + "-service-sdk-java";
        File projectDir = new File(this.projectPath);
        if (!projectDir.exists()) {
            projectDir.mkdir();
        }

        PomDefine pomDef = new PomDefine("cn.cloudtop.sdks", service + "-sdk", version);

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile("java/pom.xml.mustache");
        mustache.execute(new FileWriter(projectDir + File.separator + "pom.xml"), pomDef).flush();

        this.sourcePath = projectDir + File.separator + "src" + File.separator + "main" + File.separator + "java";
        File sourceDir = new File(this.sourcePath);
        if (!sourceDir.exists()) {
            sourceDir.mkdirs();
        }
    }

    @Override
    public void generate(ApiInfo api) throws IOException {
        this.generateFile("java/request.mustache", api.getRequest());
        this.generateFile("java/response.mustache", api.getResponse());
        for (ViewObjectClassDefine vo : api.getVos()) {
            this.generateFile("java/vo.mustache", vo);
        }
    }

    private void generateFile(String template, PojoInfo data) throws IOException {
        String packagePath = this.initPackage(data);
        String filePath = packagePath + File.separator + String.format("%s.java", data.getClassName());
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(template);
        mustache.execute(new FileWriter(filePath), data).flush();
    }

    private String initPackage(PojoInfo pojoDef) {
        String packagePath = this.sourcePath + File.separator + this.getPackagePath(pojoDef.getPackageName());
        LOGGER.info("packagePath:" + packagePath);
        File packageDir = new File(packagePath);
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }
        return packagePath;
    }

    private String getPackagePath(String packageName) {
        LOGGER.info("package is " + packageName);
        String[] dirs = packageName.split("\\.");
        String packagePath = "";
        for (String dir : dirs) {
            packagePath += dir + File.separator;
        }
        return packagePath;
    }
}
