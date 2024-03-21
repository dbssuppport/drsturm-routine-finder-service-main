package com.e2x.bigcommerce.routinefinderservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.spi.DocumentationType.OAS_30;

@Configuration
public class SwaggerSpecConfigurer {

    @Bean
    @Primary
    public SwaggerResourcesProvider swaggerResourcesProvider() {
        return () -> {
            SwaggerResource wsResource = new SwaggerResource();
            wsResource.setName("v1");
            wsResource.setSwaggerVersion(OAS_30.getName());
            wsResource.setLocation("/swagger-apis/v1/openapi.yaml");

            List<SwaggerResource> resources = new ArrayList<>();
            resources.add(wsResource);

            return resources;
        };
    }

}
