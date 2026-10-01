package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entity.Bsusers;
import com.example.demo.service.BsusersService;

@Controller
public class BsusersController {

    @Autowired
    private BsusersService bsusersService;

    // 跳轉至登入主頁面
    @GetMapping(value = "/")
    public String mainPage() {
        return "index";
    }

    // 處理 AJAX 登入請求
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Map<String, Object> doLogin(@RequestBody Bsusers bsusers, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        Bsusers loginUser = bsusersService.login(bsusers);

        if (loginUser != null) {
            session.setAttribute("loginUser", loginUser);
            result.put("success", true);
            result.put("message", "登入成功！");
            result.put("role", loginUser.getRole());
        } else {
            result.put("success", false);
            result.put("message", "帳號或密碼錯誤！");
        }
        return result;
    }

    // 導向內部系統入口首頁
    @GetMapping("/portalHome")
    public String toPortalHome(HttpSession session) {
        if (session.getAttribute("loginUser") == null) {
            return "redirect:/";
        }
        return "portalHome"; 
    }

    @GetMapping(value = "/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        if (session != null) {
            session.removeAttribute("loginUser");
            session.invalidate();
        }

        // 強制清除瀏覽器紀錄的 JSESSIONID Cookie
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}