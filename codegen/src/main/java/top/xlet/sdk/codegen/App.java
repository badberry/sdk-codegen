package top.xlet.sdk.codegen;

import com.google.common.collect.Lists;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.airlift.airline.Cli;
import io.airlift.airline.Help;
import io.swagger.models.Model;
import io.swagger.models.Path;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import top.xlet.sdk.codegen.cmds.Generate;
import top.xlet.sdk.codegen.cmds.Langs;
import top.xlet.sdk.codegen.define.*;

import java.util.List;
import java.util.Map;

/**
 * code gen app.
 */
public class App {

    public static void main(String[] args) {
        try {
            @SuppressWarnings("unchecked")
            Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("swagger-codegen-cli")
                    .withDescription("Swagger code generator CLI.")
                    .withDefaultCommand(Langs.class)
                    .withCommands(
                            Generate.class,
                            //Meta.class,
                            Langs.class,
                            Help.class
                            //ConfigHelp.class
                    );

            builder.build().parse(args).run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
