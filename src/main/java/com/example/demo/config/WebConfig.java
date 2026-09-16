package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 將網址上的 /uploads/** 請求，對應到專案根目錄底下的 uploads 資料夾
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
