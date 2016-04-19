package top.xlet.sdk.codegen.service;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by jackie on 16-4-7
 */
@Configuration
public class RequestInfoConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestInfoInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
