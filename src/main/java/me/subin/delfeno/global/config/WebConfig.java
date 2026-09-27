package me.subin.delfeno.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author 박 수 빈
 * @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.api.base-path}")
    private String basePath;

    @Value("${app.api.versioning}")
    private String versioning;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix(basePath + "/" + versioning, HandlerTypePredicate.forAnnotation(RestController.class))
                  // 요청 경로 매칭 전략 설정
                  .setPathMatcher(new AntPathMatcher())
                  // 경로 해석시 사용할 헬퍼 설정
                  .setUrlPathHelper(new UrlPathHelper());
    }
}