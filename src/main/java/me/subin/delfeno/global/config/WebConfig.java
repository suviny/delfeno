package me.subin.delfeno.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 박 수 빈
 * @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String prefix;

    public WebConfig(
            @Value("${app.api.base-path}") String basePath,
            @Value("${app.api.versioning}") String versioning) {
        this.prefix = basePath + "/" + versioning;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(prefix, HandlerTypePredicate.forAnnotation(RestController.class));
    }
}