package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
    private LoginInterceptor loginInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // 取得專案根目錄的絕對路徑，避免跨平台或不同 IDE 執行時找不到資料夾
	    String uploadDir = System.getProperty("user.dir") + "/uploads/";
	    
	    registry.addResourceHandler("/uploads/**")
	            .addResourceLocations("file:" + uploadDir);
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 攔截所有請求
                .excludePathPatterns(
                    "/",                // 登入頁面
                    "/api/login",       // 登入 API（必須放行才能登入）
                    "/logout",          // 登出 API
                    "/css/**",          // 靜態資源放行
                    "/js/**", 
                    "/images/**",
                    "/uploads/**"
                );
    }
}
		