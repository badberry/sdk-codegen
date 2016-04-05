package top.xlet.sdk.codegen;

import com.google.common.collect.Lists;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.airlift.airline.Cli;
import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
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
        String language = "Java";
        String baseDir = "/home/jackie/idea/";
        String url = "http://localhost:8090/v2/api-docs?group=sample";

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
        String responseStr = response.body().string();
        Swagger swagger = new SwaggerParser().parse(responseStr);

        Generator generator = GeneratorFactory.get(language);
        generator.initProject(baseDir, "sample", swagger.getInfo().getVersion());

        Map<String, Model> definitions = swagger.getDefinitions();
        Map<String, PojoInfo> pojos = new DefineAnalyzer()
                .definitions(definitions)
                .basePackage("cn.cloudtop.sdk.sample")
                .build();

        List<ApiInfo> apis = Lists.newArrayList();
        for (String key : swagger.getPaths().keySet()) {
            LOGGER.info("process path {}", key);
            Path path = swagger.getPath(key);

            ApiInfo api = new ApiBuilder()
                    .pojos(pojos)
                    .basePackage("cn.cloudtop.sdk.sample")
                    .path(key)
                    .build(path);
            LOGGER.info(api.toString());
            generator.generate(api);
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
