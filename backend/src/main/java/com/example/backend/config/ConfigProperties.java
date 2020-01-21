package com.example.backend.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableConfigurationProperties({ConfigProperties.class})
@ConfigurationProperties(prefix = "properties", ignoreUnknownFields = false)
public class ConfigProperties {

    private String jwtSecret;

    private int jwtExpirationInMs;

    private int maxAgeSecs;

    private String confirmSubject;

    private String examinationConfirmed;

    private int confirmExpiry;

    private String frontBaseUrl;
}
