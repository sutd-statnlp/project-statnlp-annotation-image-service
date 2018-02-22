package com.sutd.statnlp.annotationimage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private final ApplicationProperties applicationProperties;

    public SwaggerConfig(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(applicationProperties.getSwagger()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant(applicationProperties.getSwagger().getDefaultIncludePattern()))
                .build();
    }

    private ApiInfo apiInfo(ApplicationProperties.Swagger swagger) {
        return new ApiInfo(
                swagger.getTitle(),
                swagger.getDescription(),
                swagger.getVersion(),
                null,
                null,
                null,
                null,
                Collections.emptyList());
    }
}