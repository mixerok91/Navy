package by.stepanov.navy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("by.stepanov.navy.controller"))
                .build()
                .apiInfo(metaData());
    }

    protected ApiInfo metaData(){
        return new ApiInfo(
                "Sea Port Controller Service API",
                "REST API for controlling Sea ports and ships",
                "0.0.9",
                "Terms of service",
                new Contact("Me", "", "maxim.stepanov91@gmail.com"),
                "License of API", "", Collections.EMPTY_LIST
        );
    }
}
