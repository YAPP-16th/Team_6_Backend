package com.yapp.fmz.config;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String version;
    private String title;

    @Bean
    public Docket apiV1() {
        version = "V1";
        title = "FindMyZone API " + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yapp.fmz"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }


    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "YAPP 2팀 \"찾아존\" API DOCS 입니다.",
                version,
                null,
                null,
                "Licenses - YAPP WEB 2팀 SERVER: 정원희, 허진호",

                null,

                new ArrayList<>());
    }
}