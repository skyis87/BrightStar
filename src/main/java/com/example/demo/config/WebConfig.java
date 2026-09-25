package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // 取得專案根目錄的絕對路徑，避免跨平台或不同 IDE 執行時找不到資料夾
	    String uploadDir = System.getProperty("user.dir") + "/uploads/";
	    
	    registry.addResourceHandler("/uploads/**")
	            .addResourceLocations("file:" + uploadDir);
	}
}
		