package top.xlet.sdk.codegen.generators;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Maps;
import top.xlet.sdk.codegen.define.ApiInfo;
import top.xlet.sdk.codegen.define.PojoInfo;
import top.xlet.sdk.codegen.define.PomDefine;
import top.xlet.sdk.codegen.define.ViewObjectClassDefine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * java 代码生成器.
 */
public class JavaGenerator implements Generator {

    private String projectPath;
    private String sourcePath;
    private Map<String, String> baseTypeMap;

    public JavaGenerator() {
        baseTypeMap = Maps.newHashMap();
        baseTypeMap.put("int64", "long");
        baseTypeMap.put("string", "String");
        //todo: add other base type map.
    }

    @Override
    public String getType(String type) {
        if (this.baseTypeMap.containsKey(type)) {
            return this.baseTypeMap.get(type);
        } else {
            throw new RuntimeException(String.format("not support type[%s]", type));
        }
    }

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

    private void generateFile(String template, PojoInfo pojo) throws IOException {
        String packagePath = this.initPackage(pojo);
        String filePath = packagePath + File.separator + String.format("%s.java", pojo.getClassName());
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(template);
        mustache.execute(new FileWriter(filePath), pojo).flush();
    }

    private String initPackage(PojoInfo pojoDef) {
        String packagePath = this.sourcePath + File.separator + this.getPackagePath(pojoDef.getPackageName());
        File packageDir = new File(packagePath);
        if (!packageDir.exists()) {
            packageDir.mkdirs();
        }
        return packagePath;
    }

    private String getPackagePath(String packageName) {
        String[] dirs = packageName.split("\\.");
        String packagePath = "";
        for (String dir : dirs) {
            packagePath += dir + File.separator;
        }
        return packagePath;
    }
}
