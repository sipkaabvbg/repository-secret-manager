package com.cicd.credentials.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Web Configuration to handle Cross-Origin Resource Sharing (CORS).
 * Ensures the SAPUI5 frontend can communicate with backend.
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // "/**" applies CORS configuration globally
                        .allowedOrigins("*") // Allows requests from any origin/domain
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods for the API
                        .allowedHeaders("*"); // Allows all HTTP headers in the requests
            }
        };
    }
}