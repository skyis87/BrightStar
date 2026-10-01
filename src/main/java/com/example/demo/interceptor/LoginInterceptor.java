package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        
        // 檢查 Session 是否存在且有登入使用者
        if (session == null || session.getAttribute("loginUser") == null) {
            String requestURI = request.getRequestURI();
            
            // 如果是 AJAX 請求 (/api/...)，回傳 401 錯誤碼給前端，不要重定向
            if (requestURI.startsWith("/api/")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"未登入或 Session 已失效\"}");
            } else {
                // 一般頁面請求，直接導回首頁/登入頁
                response.sendRedirect("/");
            }
            return false;
        }
        return true;
    }
}