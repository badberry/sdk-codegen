package top.xlet.sdk.codegen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.airlift.airline.Cli;
import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import top.xlet.sdk.codegen.cmds.Langs;
import top.xlet.sdk.codegen.define.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * code gen app.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class App implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(App.class)
                .run(args);
    }

    public void run(String... strings) throws Exception {
        LOGGER.info("get api docs for codegen");
        String url = "http://localhost:8090/v2/api-docs?group=codegen";
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
        String responseStr = response.body().string();
        Swagger swagger = new SwaggerParser().parse(responseStr);

        Map<String, ViewObjectClassDefine> voDefMap = Maps.newHashMap();
        Map<String, ResponseClassDefine> responseDefMap = Maps.newHashMap();
        List<PropertyInfo> referenceProperties = Lists.newArrayList();

        Map<String, Model> definitions = swagger.getDefinitions();
        for (String className : definitions.keySet()) {
            Model model = definitions.get(className);
            String desc = model.getDescription();
            LOGGER.info("get definition:{},desc:{}", className, desc);

            List<PropertyInfo> classProperties = Lists.newArrayList();
            Map<String, Property> properties = model.getProperties();
            for (String propertyName : properties.keySet()) {
                Property property = properties.get(propertyName);
                String propertyDesc = property.getDescription();
                PropertyInfo propertyDef;
                if (property instanceof RefProperty) {
                    String propertyType = ((RefProperty) property).getSimpleRef();
                    LOGGER.info("get reference property:name={},type={},desc={}", propertyName, propertyType, propertyDesc);
                    propertyDef = new PropertyInfo(propertyName, propertyType, propertyDesc);
                    referenceProperties.add(propertyDef);
                } else {
                    String propertyType = property.getType();
                    LOGGER.info("get reference property:name={},type={},desc={}", propertyName, propertyType, propertyDesc);
                    propertyDef = new PropertyInfo(propertyName, propertyType, propertyDesc);
                }
                classProperties.add(propertyDef);
            }

            if (className.contains("Response")) {
                ResponseClassDefine responseClass = new ResponseClassDefine("", className, desc, classProperties);
                responseDefMap.put(className, responseClass);
                LOGGER.info(responseClass.toString());
            } else {
                ViewObjectClassDefine vo = new ViewObjectClassDefine("", className, desc, classProperties);
                voDefMap.put(className, vo);
                LOGGER.info(vo.toString());
            }
        }

        for (PropertyInfo propertyDef : referenceProperties) {
            String type = propertyDef.getType();
            if (voDefMap.containsKey(type)) {
                PojoInfo ref = voDefMap.get(type);
                propertyDef.setRef(ref);
            } else if (responseDefMap.containsKey(type)) {
                PojoInfo ref = responseDefMap.get(type);
                propertyDef.setRef(ref);
            } else {
                LOGGER.error("reference type={} not found!", type);
            }
        }

        List<ApiInfo> apis = Lists.newArrayList();
        for (String key : swagger.getPaths().keySet()) {
            LOGGER.info("process path {}", key);
            Path path = swagger.getPath(key);

            ApiInfo api = new ApiBuilder()
                    .responses(responseDefMap)
                    .vos(voDefMap)
                    .path(key)
                    .build(path);

            LOGGER.info("path.get is {}", path.getGet());
            //ApiInfo api = new ApiInfo(key,request,response,vos);
        }

    }

    private void get(String... args) throws IOException {

        @SuppressWarnings("unchecked")
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("swagger-codegen-cli")
                .withDescription("Swagger code generator CLI. More info on swagger.io")
                .withDefaultCommand(Langs.class)
                .withCommands(
                        //Generate.class,
                        //Meta.class,
                        Langs.class
                        //Help.class,
                        //ConfigHelp.class
                );

        builder.build().parse(args).run();
    }
}
