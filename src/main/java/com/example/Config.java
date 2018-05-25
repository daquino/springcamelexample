package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class Config {
    @Bean
    public Map<String, RouteController.RouteInfo> routeInfoRegistry() {
        return new ConcurrentHashMap<>();
    }
}
