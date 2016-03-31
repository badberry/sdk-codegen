package top.xlet.sdk.codegen;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.airlift.airline.Cli;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import top.xlet.sdk.codegen.cmds.Langs;

import java.io.IOException;

/**
 * code gen app.
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(App.class)
                .run(args);
    }

    public void run(String... args) throws Exception {

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

    private void get() throws IOException {
        String url = "http://localhost:8090/v2/api-docs?group=codegen";
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(new Request.Builder().url(url).get().build()).execute();
        String responseStr = response.body().string();
        Swagger swagger = new SwaggerParser().parse(responseStr);
    }
}
