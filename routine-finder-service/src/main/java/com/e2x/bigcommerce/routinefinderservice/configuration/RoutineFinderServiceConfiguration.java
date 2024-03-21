package com.e2x.bigcommerce.routinefinderservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "routine-finder-service")
public class RoutineFinderServiceConfiguration {
    private String bucketName;
}
