package top.xlet.sdk.codegen;

import com.google.common.collect.Lists;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.airlift.airline.Cli;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
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
import top.xlet.sdk.codegen.define.ApiInfo;

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

        List<ApiInfo> apis = Lists.newArrayList();
        for (String key : swagger.getPaths().keySet()) {
            LOGGER.info("process path {}", key);
            Path path = swagger.getPath(key);

            Map<HttpMethod, Operation> operationMap = path.getOperationMap();
            if (operationMap.size() > 1) {
                LOGGER.error("api {} not set method", key);
            }

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
