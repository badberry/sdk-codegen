package top.xlet.sdk.codegen.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 *
 */
@EnableSwagger2
@Configuration
public class ApiDocsConfig {

    @Bean
    public Docket apiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("sample")
                .apiInfo(apiInfo())
                .select()
                .paths(regex("/simple.*"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring boot Rest simple sample with Swagger2")
                .description("Spring boot Rest simple sample with Swagger2")
                .contact("jackie@xlet.top")
                .version("1.0")
                .build();
    }
}
