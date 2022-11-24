package com.perfectrum.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8083","http://localhost:3000","http://localhost:80","http://j7c105.p.ssafy.io:8083","http://j7c105.p.ssafy.io","http://j7c105.p.ssafy.io:80")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS")
                .maxAge(6000)
                .allowCredentials(true);
    }
}
