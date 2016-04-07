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
import top.xlet.sdk.codegen.cmds.Langs;
import top.xlet.sdk.codegen.define.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * code gen app.
 */
public class App {

    public static void main(String[] args) {
        @SuppressWarnings("unchecked")
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("swagger-codegen-cli")
                .withDescription("Swagger code generator CLI. More info on swagger.io")
                .withDefaultCommand(Langs.class)
                .withCommands(
                        Generate.class,
                        //Meta.class,
                        Langs.class
                        //Help.class,
                        //ConfigHelp.class
                );

        builder.build().parse(args).run();
    }

    public void run(String... strings) throws Exception {
        System.out.println("get api docs for codegen");
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
                .generator(generator)
                .build();

        List<ApiInfo> apis = Lists.newArrayList();
        for (String key : swagger.getPaths().keySet()) {
            System.out.println(String.format("process path %s", key));
            Path path = swagger.getPath(key);
            new ApiBuilder()
                    .pojos(pojos)
                    .basePackage("cn.cloudtop.sdk.sample")
                    .path(key)
                    .generator(generator)
                    .build(path);
        }

    }
}
