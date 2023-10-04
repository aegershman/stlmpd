package com.aegershman.stlmpd;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties("stlmpd")
@Configuration
public class StlmpdProperties {
    private String apiUrl;
    private Long pollIntervalMillis;
    private String geocodeApiToken;
    private String geocodeApiUrlBase;
}
