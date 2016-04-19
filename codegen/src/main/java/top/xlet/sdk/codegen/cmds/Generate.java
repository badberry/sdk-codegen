package top.xlet.sdk.codegen.cmds;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.airlift.airline.Command;
import io.airlift.airline.Option;
import io.swagger.models.*;
import io.swagger.parser.SwaggerParser;
import top.xlet.sdk.codegen.define.*;
import top.xlet.sdk.codegen.generators.ApiBuilder;
import top.xlet.sdk.codegen.generators.DefineAnalyzer;
import top.xlet.sdk.codegen.generators.Generator;
import top.xlet.sdk.codegen.generators.GeneratorFactory;

import java.io.File;
import java.util.Map;

/**
 *
 */
@Command(name = "generate", description = "Generator code with given language")
public class Generate implements Runnable {

    @Option(name = {"-l", "--lang"}, title = "language", required = true,
            description = "client language to generate (see lang command, required)")
    private String lang;

    @Option(name = {"-o", "--output"}, title = "output directory",
            description = "where to write the generated files (current dir by default)")
    private String output = new File(".").getAbsolutePath();

    @Option(name = {"-s", "--service"}, title = "service", required = true,
            description = "service to generate")
    private String service = "";

    @Option(name = {"-h", "--host"}, title = "host", required = true, description = "service host")
    private String host = "";

    @Option(name = {"-p", "--port"}, title = "port", description = "service listening port(80 by default)")
    private String port = "80";

    @Option(name = {"-pg", "--package"}, title = "package", description = "base package for sdk")
    private String pack = "cn.cloudtop.sdk";

    @Override
    public void run() {
        System.out.println(String.format("params:l=%s,o=%s,s=%s,h=%s,p=%s",
                this.lang, this.output, this.service, this.host, this.port));

        String apiDocUrl = String.format("http://%s:%s/v2/api-docs?group=%s", this.host, this.port, this.service);

        System.out.println(String.format("get api docs form %s", apiDocUrl));
        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(new Request.Builder().url(apiDocUrl).get().build()).execute();
            if (response.code() == 200) {
                System.out.println("get api docs success,try to analyse api docs");
            } else {
                System.out.println("get api docs failed! make sure that the service api docs has existed!");
                return;
            }
            String responseStr = response.body().string();
            Swagger swagger = new SwaggerParser().parse(responseStr);
            if (swagger != null && swagger.getInfo() != null) {
                System.out.println("api docs analyse success,try to generate sdk files");
            } else {
                System.out.println("api docs analyse error,make sure that the service api doc generate by swagger!");
                return;
            }
            Generator generator = GeneratorFactory.get(this.lang.toUpperCase());
            generator.initProject(this.output, this.service, swagger.getInfo().getVersion());

            String packageName = String.format("%s.%s", this.pack, this.service);
            Map<String, Model> definitions = swagger.getDefinitions();
            Map<String, PojoInfo> pojos = new DefineAnalyzer()
                    .definitions(definitions)
                    .basePackage(packageName)
                    .generator(generator)
                    .build();

            for (String url : swagger.getPaths().keySet()) {
                System.out.println(String.format("process url %s", url));
                Path path = swagger.getPath(url);
                Map<HttpMethod, Operation> operationMap = path.getOperationMap();
                if (operationMap.size() > 1) {
                    throw new RuntimeException(String.format("api %s not set method", url));
                }
                HttpMethod httpMethod = operationMap.keySet().toArray(new HttpMethod[operationMap.size()])[0];
                Operation operation = operationMap.values().toArray(new Operation[operationMap.size()])[0];
                new ApiBuilder()
                        .pojos(pojos)
                        .basePackage(packageName)
                        .url(url)
                        .generator(generator)
                        .method(httpMethod)
                        .operation(operation)
                        .build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
